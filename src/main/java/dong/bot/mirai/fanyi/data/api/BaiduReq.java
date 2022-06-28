package dong.bot.mirai.fanyi.data.api;

import dong.bot.mirai.fanyi.data.conf.PluginConfig;
import dong.bot.mirai.fanyi.enums.Languages;
import dong.bot.mirai.fanyi.util.CharUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度 API 的请求参数模型
 *
 * @author dong
 */
public class BaiduReq {

    /**
     * 构造请求实体
     *
     * @param query     需翻译文本
     * @param from      原文语言
     * @param to        译文语言
     * @param appId     开发者 AppId
     * @param secretKey 开发者密钥
     * @return 请求参数
     */
    @NotNull
    public static Map<String, Object> basic(
            @NotNull String query,
            @NotNull Languages from,
            @NotNull Languages to,
            @NotNull String appId,
            @NotNull String secretKey
    ) {
        if (CharUtil.isBlank(query) || to == Languages.Auto)
            return Collections.emptyMap();
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = DigestUtils.md5Hex(appId + query + salt + secretKey);
        Map<String, Object> map = new HashMap<>(6);
        map.put("q", query);
        map.put("from", from.code);
        map.put("to", to.code);
        map.put("appid", appId);
        map.put("salt", salt);
        map.put("sign", sign);
        return map;
    }

    /**
     * 构造请求实体
     * 从配置中读取 appId 和密钥
     *
     * @param query 需翻译文本
     * @param from  原文语言
     * @param to    译文语言
     * @return 请求参数
     */
    @NotNull
    public static Map<String, Object> basic(@NotNull String query, @NotNull Languages from, @NotNull Languages to) {
        if (CharUtil.isBlank(query) || to == Languages.Auto)
            return Collections.emptyMap();
        var appId = PluginConfig.INSTANCE.getBaiduAppId();
        var secret = PluginConfig.INSTANCE.getBaiduSecretKey();
        return basic(query, from, to, appId, secret);
    }

    /**
     * 构造请求实体（升级版）
     *
     * @param query     需翻译文本
     * @param from      原文语言
     * @param to        译文语言
     * @param appId     开发者 AppId
     * @param secretKey 开发者密钥
     * @param tts       是否显示语音合成资源：0-显示，1-不显示
     * @param dict      是否显示词典资源：0-显示，1-不显示
     * @param action    判断是否需要使用自定义术语干预 API：0-否，1-是
     * @return 请求参数
     */
    @NotNull
    public Map<String, Object> pro(
            @NotNull String query,
            @NotNull Languages from,
            @NotNull Languages to,
            @NotNull String appId,
            @NotNull String secretKey,
            boolean tts,
            boolean dict,
            boolean action
    ) {
        Map<String, Object> map = basic(query, from, to, appId, secretKey);
        map.put("tts", tts ? 1 : 0);
        map.put("dict", dict ? 1 : 0);
        map.put("action", action ? 1 : 0);
        return map;
    }

    /**
     * 构造请求实体（升级版）
     * 从配置中读取 appId 和密钥
     *
     * @param query  需翻译文本
     * @param from   原文语言
     * @param to     译文语言
     * @param tts    是否显示语音合成资源：0-显示，1-不显示
     * @param dict   是否显示词典资源：0-显示，1-不显示
     * @param action 判断是否需要使用自定义术语干预 API：0-否，1-是
     * @return 请求参数
     */
    @NotNull
    public Map<String, Object> pro(
            @NotNull String query,
            @NotNull Languages from,
            @NotNull Languages to,
            boolean tts,
            boolean dict,
            boolean action
    ) {
        Map<String, Object> map = basic(query, from, to);
        map.put("tts", tts ? 1 : 0);
        map.put("dict", dict ? 1 : 0);
        map.put("action", action ? 1 : 0);
        return map;
    }
}
