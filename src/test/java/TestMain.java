import com.fasterxml.jackson.databind.ObjectMapper;
import dong.bot.mirai.fanyi.data.api.BaiduReq;
import dong.bot.mirai.fanyi.data.api.BaiduResp;
import dong.bot.mirai.fanyi.enums.Languages;
import dong.bot.mirai.fanyi.util.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static dong.bot.mirai.fanyi.Const.BaiduParam.*;

public class TestMain {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    /**
     * 需要在项目根目录创建 app.txt 文件，并写入appId 和密钥，各占一行
     */
    private static List<String> getAppKey() throws Exception {
        List<String> lines = Files.readAllLines(Path.of("app.txt"));
        if (lines.size() < 3) throw new Exception("参数不足。行：[appid, 密钥, API地址]");
        return lines;
    }

    @Test
    public void simple() throws Exception {
        List<String> ls = getAppKey();
        var appId = ls.get(0);
        var secretKey = ls.get(1);
        var api = ls.get(2);
        var query = "hello\nworld";
        var salt = String.valueOf(System.currentTimeMillis());
        var sign = DigestUtils.md5Hex(appId + query + salt + secretKey);
        var form = BaiduReq.basic(query, Languages.Auto, Languages.Zh);
        form.put(APPID, appId);
        form.put(SALT, salt);
        form.put(SIGN, sign);
        Optional<String> response = HttpUtil.post(api, form);
        if (response.isEmpty()) {
            System.out.println("no response");
            return;
        }
        BaiduResp body = JACKSON.readValue(response.get(), BaiduResp.class);
        if (!body.success()) {
            System.out.printf("%d--%s\n", body.getErrorCode(), body.getErrorMsg());
            return;
        }
        body.getTransResult().forEach(r -> System.out.printf("%s -> %s\n", r.src(), r.dst()));
    }

}
