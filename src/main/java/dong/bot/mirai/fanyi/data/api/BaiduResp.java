package dong.bot.mirai.fanyi.data.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import dong.bot.mirai.fanyi.enums.api.BaiduRespCode;
import kotlinx.serialization.Serializable;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 百度 API 的响应模型
 *
 * @author dong
 */
@Serializable
public class BaiduResp {
    /**
     * 原文语言；用户指定，或者自动检测出的语言（设为auto时）
     */
    private String from;
    /**
     * 译文语言
     */
    private String to;
    /**
     * 翻译结果集合
     */
    @JsonProperty("trans_result")
    private List<TransResult> transResult;
    /**
     * 错误码
     */
    @JsonProperty("error_code")
    private int errorCode;
    /**
     * 异常消息
     */
    @JsonProperty("error_msg")
    private String errorMsg;
    /**
     * 原文 tts 链接；MP3 格式
     */
    @JsonProperty("src_tts")
    private String srcTts;
    /**
     * 译文 tts 链接；MP3 格式
     */
    @JsonProperty("dst_tts")
    private String dstTts;
    /**
     * 中英词典资源；中文或英文词典资源，包含音标；简明释义等内容
     */
    private String dict;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResult> getTransResult() {
        return transResult;
    }

    public void setTransResult(List<TransResult> transResult) {
        this.transResult = transResult;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSrcTts() {
        return srcTts;
    }

    public void setSrcTts(String srcTts) {
        this.srcTts = srcTts;
    }

    public String getDstTts() {
        return dstTts;
    }

    public void setDstTts(String dstTts) {
        this.dstTts = dstTts;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    /**
     * 翻译结果
     */
    public record TransResult(
            // 原文
            String src,
            // 译文
            String dst
    ) {
    }

    /**
     * 获取响应代码枚举
     */
    public Optional<BaiduRespCode> getCode() {
        return BaiduRespCode.by(errorCode);
    }

    /**
     * 是否请求成功
     */
    public boolean success() {
        Optional<BaiduRespCode> code = getCode();
        return code.isEmpty() || code.get() == BaiduRespCode.Success;
    }

    /**
     * 记录请求异常
     *
     * @param logger mirai 日志接口
     */
    public void logMessage(@NotNull MiraiLogger logger) {
        Optional<BaiduRespCode> opt = getCode();
        if (opt.isEmpty())
            return;

        BaiduRespCode error = opt.get();
        logger.warning(String.format(
                "异常: %s :: %s :: %s",
                getErrorCode(),
                getErrorMsg(),
                error.getMessage()
        ));
    }
}
