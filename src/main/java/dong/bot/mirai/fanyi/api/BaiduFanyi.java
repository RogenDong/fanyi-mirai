package dong.bot.mirai.fanyi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dong.bot.mirai.fanyi.FanYi;
import dong.bot.mirai.fanyi.Translator;
import dong.bot.mirai.fanyi.data.api.BaiduReq;
import dong.bot.mirai.fanyi.data.api.BaiduResp;
import dong.bot.mirai.fanyi.data.conf.PluginConfig;
import dong.bot.mirai.fanyi.enums.Languages;
import dong.bot.mirai.fanyi.util.CharUtil;
import dong.bot.mirai.fanyi.util.HttpUtil;
import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static dong.bot.mirai.fanyi.Const.BaiduParam.*;

/**
 * 外部接口：百度翻译
 *
 * @author dong
 */
public class BaiduFanyi implements Translator {

    // 日志
    private static final MiraiLogger LOGGER = FanYi.LOGGER;
    // 配置对象
    private static final PluginConfig CONF = PluginConfig.INSTANCE;
    // 原文字符串最大长度
    private static final int ORIGINAL_MAX_LEN = 20000;
    // 指定 api
    private String name;
    // 接口地址
    private String api;
    // 开发者 app-id
    private String appId;
    // 开发者密钥
    private String secretKey;
    // JSON 工具
    private static final ObjectMapper JSON = new ObjectMapper();

    /**
     * 指定配置名获取 API 实例
     *
     * @param name 配置名
     */
    public BaiduFanyi(@NotNull String name) {
        if (CONF.unavailable(name)) {
            LOGGER.error(String.format("API 实例【%s】未做好相关配置！", name));
            return;
        }
        this.name = name;
        api = CONF.getApis().get(name);
        appId = CONF.getAppIds().get(name);
        secretKey = CONF.getSecretKeys().get(name);
        LOGGER.info("百度翻译 for " + appId);
    }

    /**
     * 使用配置
     */
    public BaiduFanyi() {
        this("baidu");
    }

    /**
     * 指定开发者
     *
     * @param appId     开发者 app id
     * @param secretKey 开发者密钥
     */
    public BaiduFanyi(@NotNull String name, @NotNull String api, @NotNull String appId, @NotNull String secretKey) {
        if (CONF.unavailable(name)) {
            LOGGER.error(String.format("API 实例【%s】未做好相关配置！", name));
            return;
        }
        this.api = api;
        this.name = name;
        this.appId = appId;
        this.secretKey = secretKey;
        LOGGER.info("百度翻译 for " + appId);
    }

    @Override
    public Optional<String> getName() {
        if (CharUtil.isBlank(name))
            return Optional.empty();
        return Optional.of(name);
    }

    @Override
    public int getOriginalMaxLength() {
        return ORIGINAL_MAX_LEN;
    }

    @NotNull
    public String getAppId() {
        return appId;
    }

    private List<BaiduResp.TransResult> getResponse(String response) {
        if (CharUtil.isBlank(response)) {
            LOGGER.warning("翻译 API 无响应！");
            return Collections.emptyList();
        }
        try {
            BaiduResp body = JSON.readValue(response, BaiduResp.class);
            if (body.success()) {
                return body.getTransResult();
            }
            body.logMessage(LOGGER);
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON 反序列化失败！raw:\n" + response);
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private List<BaiduResp.TransResult> translate(@NotNull Map<String, Object> param) {
        if (param.isEmpty())
            return Collections.emptyList();
        String query = param.get(QUERY).toString();
        var salt = String.valueOf(System.currentTimeMillis());
        var sign = DigestUtils.md5Hex(appId + query + salt + secretKey);
        param.put(APPID, appId);
        param.put(SALT, salt);
        param.put(SIGN, sign);
        Optional<String> response = HttpUtil.post(api, param);
        return response.map(this::getResponse).orElse(Collections.emptyList());
    }

    @Override
    public @NotNull Optional<String> to(@NotNull String original, @NotNull Languages fromLang, @NotNull Languages toLang) {
        Map<String, Object> param = BaiduReq.basic(original, fromLang, toLang);
        List<BaiduResp.TransResult> results = translate(param);
        if (results.isEmpty())
            return Optional.empty();
        // TODO 词典、语音合成
        String dst = results.stream().map(r -> r.dst())
                .collect(Collectors.joining("\n"));
        return Optional.of(dst);
    }

    @Override
    public @NotNull Optional<String> auto(@NotNull String original) {
        return to(original, Languages.Auto, Languages.Zh);
    }
}
