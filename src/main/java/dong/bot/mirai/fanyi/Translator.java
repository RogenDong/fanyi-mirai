package dong.bot.mirai.fanyi;

import dong.bot.mirai.fanyi.enums.Languages;

import static dong.bot.mirai.fanyi.Const.Tip.TODO;

/**
 * 翻译
 * TODO 实现请求 API 并处理结果
 *
 * @author dong
 */
public abstract class Translator {

    /**
     * 自动识别原文语言并翻译为中文
     *
     * @param original 原文
     * @return 中文译文
     */
    public static String auto(String original) {
        return TODO;
    }

    /**
     * 指定原文语言，翻译为中文
     *
     * @param original 原文
     * @param fromLang 原文语言
     * @return 中文译文
     */
    public static String from(String original, Languages fromLang) {
        return TODO;
    }

    /**
     * 指定翻译目标语言<br/>
     * - 自动识别原文语种<br/>
     *
     * @param original 中文原文
     * @param toLang   目标语言
     * @return 译文
     */
    public static String to(String original, Languages toLang) {
        return TODO;
    }

    /**
     * 指定原文语言和目标语言并翻译
     *
     * @param original 原文
     * @param fromLang 原文语言
     * @param toLang   译文语言
     * @return 译文
     */
    public static String to(String original, Languages fromLang, Languages toLang) {
        return TODO;
    }

}
