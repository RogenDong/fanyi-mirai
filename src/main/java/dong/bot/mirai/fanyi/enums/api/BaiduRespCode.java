package dong.bot.mirai.fanyi.enums.api;

import java.util.Optional;

/**
 * 百度 API 的异常代码枚举
 *
 * @author dong
 */
public enum BaiduRespCode {
    Success(52000),
    Timeout(52001),
    ApiError(52002),
    Unauth(52003),
    InvalidArgs(54000),
    SignWrong(54001),
    ReqLimited(54003),
    NeedRecharge(54004),
    LongQueryLimited(54005),
    InvalidIP(58000),
    NotSupportedTo(58001),
    ApiClosed(58002),
    CertificationNotEffective(90107),
    ;

    private final int code;
    BaiduRespCode(int code) {
        this.code = code;
    }

    /**
     * 通过 code 获取枚举
     *
     * @param code 代码
     * @return 匹配的枚举；或空引用
     */
    public static Optional<BaiduRespCode> by(int code) {
        for (BaiduRespCode val : values()) {
            if (val.code == code)
                return Optional.of(val);
        }
        return Optional.empty();
    }

    /**
     * 取消息
     */
    public String getMessage() {
        String msg;
        switch (this) {
            case Success -> msg = "成功";
            case Timeout -> msg = "请求超时";
            case ApiError -> msg = "API 系统错误";
            case Unauth -> msg = "未授权";
            case InvalidArgs -> msg = "必填参数为空";
            case SignWrong -> msg = "签名错误";
            case ReqLimited -> msg = "访问频率受限";
            case NeedRecharge -> msg = "账户余额不足";
            case LongQueryLimited -> msg = "长query请求频繁";
            case InvalidIP -> msg = "客户端IP非法";
            case NotSupportedTo -> msg = "译文语言方向不支持";
            case ApiClosed -> msg = "服务当前已关闭";
            case CertificationNotEffective -> msg = "认证未通过或未生效";
            default -> msg = "未知错误";
        }
        return msg;
    }

    /**
     * 取消息
     *
     * @param code 代码
     */
    public static String getMessageBy(int code) {
        var tmp = by(code);
        if (tmp.isEmpty())
            return "该 code 未定义";
        return tmp.get().getMessage();
    }
}
