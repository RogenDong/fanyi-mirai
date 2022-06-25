package dong.bot.mirai.fanyi.util;

import dong.bot.mirai.fanyi.FanYi;
import net.mamoe.mirai.utils.MiraiLogger;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    /**
     * 最大连接时间
     */
    public static final int CONNECTION_TIMEOUT = 5;
    /**
     * OkHTTP线程池最大空闲线程数
     */
    public static final int MAX_IDLE_CONNECTIONS = 100;
    /**
     * OkHTTP线程池空闲线程存活时间
     */
    public static final long KEEP_ALIVE_DURATION = 30L;
    /**
     * JSON格式
     */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * HTTP 客户端
     */
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
            .build();

    // 日志
    private static final MiraiLogger LOGGER = FanYi.LOGGER;

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    private static void buildHeader(final Request.Builder builder, Map<String, String> headers) {
        if (headers.isEmpty())
            return;
        headers.entrySet().stream()
                .filter(e -> !CharUtil.anyNullOrEmpty(e.getKey(), e.getValue()))
                .forEach(entry -> builder.addHeader(entry.getKey(), entry.getValue()));
    }

    /**
     * 发送请求
     *
     * @return 请求结果
     * @throws IOException 响应读写异常
     */
    private static Optional<String> call(Request.Builder builder) throws IOException {
        Request request = builder.build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful()) {
            var result = Objects.requireNonNull(response.body()).string();
            return Optional.of(result);
        }
        return Optional.empty();
    }

    /**
     * get 请求
     *
     * @return 请求结果
     */
    public static Optional<String> get(String url) {
        try {
            var builder = new Request.Builder().url(url);
            return call(builder);
        } catch (Exception e) {
            LOGGER.warning("get 请求失败！url=" + url, e);
        }
        return Optional.empty();
    }

    /**
     * get 请求
     *
     * @param headers 请求头
     * @return 请求结果
     */
    public static Optional<String> get(String url, Map<String, String> headers) {
        try {
            var builder = new Request.Builder().url(url);
            buildHeader(builder, headers);
            return call(builder);
        } catch (Exception e) {
            LOGGER.warning("get 请求失败！url=" + url, e);
        }
        return Optional.empty();
    }


    /**
     * post 请求，发送 form 表单
     *
     * @param params form
     * @return 请求结果
     */
    public static Optional<String> post(String url, Map<String, String> params) {
        try {
            FormBody.Builder formBody = new FormBody.Builder();
            if (!params.isEmpty())
                params.forEach(formBody::add);
            return call(new Request.Builder().url(url).post(formBody.build()));
        } catch (Exception e) {
            LOGGER.warning("post 请求失败! url=" + url, e);
        }
        return Optional.empty();
    }

    /**
     * post 请求，发送 JSON
     *
     * @param headers 请求头
     * @return 请求结果
     */
    public static Optional<String> post(String url, String json, Map<String, String> headers) {
        try {
            var body = RequestBody.create(json, MEDIA_TYPE_JSON);
            var builder = new Request.Builder();
            buildHeader(builder, headers);
            return call(builder.url(url).post(body));
        } catch (Exception e) {
            LOGGER.warning("post 请求失败! url=" + url, e);
        }
        return Optional.empty();
    }
}
