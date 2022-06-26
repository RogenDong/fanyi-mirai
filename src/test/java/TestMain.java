import com.fasterxml.jackson.databind.ObjectMapper;
import dong.bot.mirai.fanyi.data.api.BaiduReq;
import dong.bot.mirai.fanyi.data.api.BaiduResp;
import dong.bot.mirai.fanyi.enums.Languages;
import dong.bot.mirai.fanyi.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TestMain {

    private static final String API = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    private static final ObjectMapper JACKSON = new ObjectMapper();

    /**
     * 需要在项目根目录创建 app.txt 文件，并写入appId 和密钥，各占一行
     */
    private static List<String> getAppKey() throws Exception {
        return Files.readAllLines(Path.of("app.txt"));
    }

    @Test
    public void simple() throws Exception {
        List<String> ls = getAppKey();
        var appId = ls.get(0);
        var secretKey = ls.get(1);
        var query = "hello word";
        var form = BaiduReq.basic(query, Languages.Zh, Languages.Auto, appId, secretKey);
        Optional<String> response = HttpUtil.post(API, form);
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
