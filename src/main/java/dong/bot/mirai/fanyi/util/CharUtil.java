package dong.bot.mirai.fanyi.util;

/**
 * 字符、字符串工具类
 *
 * @author dong
 */
public interface CharUtil {

    /**
     * 是否小写英文字母
     *
     * @param c 英文字母
     * @return 是否小写
     */
    default boolean isLow(char c) {
        return 'a' <= c && c <= 'z';
    }

    /**
     * 是否大写英文字母
     *
     * @param c 英文字母
     * @return 是否大写
     */
    default boolean isUp(char c) {
        return 'A' <= c && c <= 'Z';
    }

    /**
     * 字母转小写
     *
     * @param c 大写字母
     * @return 小写字母
     */
    default char low(char c) {
        return isLow(c) ? (char)(c + 32) : c;
    }

    /**
     * 字母转大写
     *
     * @param c 小写字母
     * @return 大写字母
     */
    default char up(char c) {
        return isLow(c) ? (char)(c - 32) : c;
    }

    /**
     * 首字母大写；返回新字符串
     *
     * @param original 原字符串
     * @return 首字母大写的新字符串
     */
    default String upperCaseInitials(String original) {
        char[] chars = original.toCharArray();
        chars[0] = up(chars[0]);
        return String.valueOf(chars);
    }
}
