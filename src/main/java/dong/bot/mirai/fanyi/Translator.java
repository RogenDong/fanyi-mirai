package dong.bot.mirai.fanyi;

import dong.bot.mirai.fanyi.enums.Languages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * 翻译
 * TODO 实现请求 API 并处理结果
 *
 * @author dong
 */
public interface Translator {

    /**
     * 获取实例名
     */
    Optional<String> getName();

    /**
     * 自动识别原文语言并翻译为中文
     *
     * @param original 原文
     * @return 中文译文
     */
    @NotNull
    Optional<String> auto(@NotNull String original);

    /**
     * 指定原文语言和目标语言并翻译
     *
     * @param original 原文
     * @param fromLang 原文语言
     * @param toLang   译文语言
     * @return 译文
     */
    @NotNull
    Optional<String> to(@NotNull String original, @NotNull Languages fromLang, @NotNull Languages toLang);

}
