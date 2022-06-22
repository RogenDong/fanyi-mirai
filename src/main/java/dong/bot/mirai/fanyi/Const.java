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
    String LANG_SPLIT = "?";

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
         * 查询翻译的关键词（含空格）
         */
        String TRANS = "ff ";

        /**
         * 查看帮助的关键词
         */
        String HELP = "fhh";

        /**
         * 查看支持语言的关键词
         */
        String LANG = "fll";
    }

    /**
     * 文案
     */
    interface Tip {

        String CMD = Keyword.TRANS + "——翻译\n" + Keyword.LANG + "——查看支持语言\n" + Keyword.HELP + "——回复本消息";

        /**
         * 帮助文案
         */
        String HELP = Keyword.TRANS + "[原文语言" + LANG_SPLIT + "译文语言] <需要翻译的文本>\n" +
                "例：\n" + Keyword.TRANS + "en" + LANG_SPLIT + "zh translate <- 英文翻译为中文\n" +
                Keyword.TRANS + LANG_SPLIT + "jp translate <- 自动识别原文并翻译为日文\n" +
                Keyword.TRANS + "大丈夫です <- 自动识别原文并翻译为中文";

        String LACK_ARG = "查询缺少关键信息。";

        String TODO = "TODO";
    }
}
