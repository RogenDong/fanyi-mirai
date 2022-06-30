package dong.bot.mirai.fanyi;

/**
 * 常用字面量
 *
 * @author dong
 */
public interface Const {
    String EMPTY = "";

    String SPACE = " ";

    /**
     * 语言参数的分隔符
     */
    String LANG_DELIMITER = "?";

    /**
     * 正则：匹配空格2个或以上
     */
    String SPACES_REGEX = " {2,}";

    /**
     * 加密算法：MD5
     */
    String DIGEST_MD5 = "md5";

    /**
     * 关键词
     */
    interface Keyword {
        /**
         * 查询翻译的关键词
         */
        String TRANS = "ff";

        /**
         * 查看帮助的关键词
         */
        String TIP = "fhh";

        /**
         * 查看支持语言的关键词
         */
        String LANG = "fll";
    }

    /**
     * 文案
     */
    interface Tip {

        String KEYWORD = Keyword.TRANS + "——翻译\n" + Keyword.LANG + "——查看支持语言\n" + Keyword.TIP + "——回复本消息";

        /**
         * 帮助文案
         */
        String TRANS = Keyword.TRANS + " [原文语言" + LANG_DELIMITER + "译文语言] <需要翻译的文本>\n" +
                Keyword.TRANS + " ru" + LANG_DELIMITER + "zh Пролетарии всех стран, соединяйтесь! # 俄语翻译为中文\n" +
                Keyword.TRANS + " en" + LANG_DELIMITER + " translate # 英文翻译为中文\n" +
                Keyword.TRANS + SPACE + LANG_DELIMITER + "ru translate # 自动识别并翻译为俄语\n" +
                Keyword.TRANS + SPACE + LANG_DELIMITER + " 私たちは連合します! # 自动识别并翻译为中文\n" +
                Keyword.TRANS + " соединяйтесь! # 自动翻译为中文，或根据之前指定的语言翻译";

        String LACK_ARG = "查询缺少关键信息。";

        String TODO = "TODO";
    }

    /**
     * 百度接口参数
     */
    interface BaiduParam {
        String QUERY = "q";
        String FROM = "from";
        String TO = "to";
        String APPID = "appid";
        String SALT = "salt";
        String SIGN = "sign";
        String TTS = "tts";
        String DICT = "dict";
        String ACTION = "action";
    }

    /**
     * 描述 Http 请求或响应正文的内容类别
     */
    interface MediaType {
        String CONTENT_TYPE = "Content-Type";

        String TYPE_FORM = "application/x-www-form-urlencoded";

        String TYPE_JSON = "application/json; charset=utf-8";
    }
}
