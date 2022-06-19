package dong.bot.mirai.fanyi.util;

import java.util.Random;

/**
 * 加密工具
 *
 * @author dong
 */
public class EncryptionUtil {
    private static final Random RAND = new Random();

    public static long random() {
        return (long)((Long.MAX_VALUE - 112233) * RAND.nextDouble()) + 112233;
    }

    // TODO MD5
}
