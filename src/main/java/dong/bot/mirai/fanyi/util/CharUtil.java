package dong.bot.mirai.fanyi.util;

import org.jetbrains.annotations.NotNull;

/**
 * 字符、字符串工具类
 *
 * @author dong
 */
public abstract class CharUtil {

    /**
     * 是否小写英文字母
     *
     * @param c 英文字母
     * @return 是否小写
     */
    public static boolean isLow(char c) {
        return 'a' <= c && c <= 'z';
    }

    /**
     * 是否大写英文字母
     *
     * @param c 英文字母
     * @return 是否大写
     */
    public static boolean isUp(char c) {
        return 'A' <= c && c <= 'Z';
    }

    /**
     * 字母转小写
     *
     * @param c 大写字母
     * @return 小写字母
     */
    public static char low(char c) {
        return isLow(c) ? (char) (c + 32) : c;
    }

    /**
     * 字母转大写
     *
     * @param c 小写字母
     * @return 大写字母
     */
    public static char up(char c) {
        return isLow(c) ? (char) (c - 32) : c;
    }

    /**
     * 首字母大写；返回新字符串
     *
     * @param original 原字符串
     * @return 首字母大写的新字符串
     */
    public static @NotNull String upperCaseInitials(@NotNull String original) {
        char[] chars = original.toCharArray();
        chars[0] = up(chars[0]);
        return String.valueOf(chars);
    }
}
