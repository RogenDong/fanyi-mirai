package dong.bot.mirai.fanyi.enums;

import org.jetbrains.annotations.NotNull;

/**
 * 语种
 * TODO 支持更多语言
 *
 * @author dong
 */
public enum Languages {
    /**
     * 自动
     */
    Auto("自动", "auto"),
    /**
     * 阿拉伯语
     */
    Ara("阿拉伯", "ara"),
    /**
     * 保加利亚语
     */
    Bul("保加利亚", "bul"),
    /**
     * 繁体
     */
    Cht("繁体", "cht"),
    /**
     * 捷克语
     */
    Cs("捷克", "cs"),
    /**
     * 丹麦语
     */
    Dan("丹麦", "dan"),
    /**
     * 德语
     */
    De("德语", "de"),
    /**
     * 希腊语
     */
    El("希腊", "el"),
    /**
     * 英语
     */
    En("英语", "en"),
    /**
     * 爱沙尼亚语
     */
    Est("爱沙尼亚", "est"),
    /**
     * 芬兰语语
     */
    Fin("芬兰", "fin"),
    /**
     * 法语
     */
    Fra("法语", "fra"),
    /**
     * 匈牙利语
     */
    Hu("匈牙利", "hu"),
    /**
     * 意大利语
     */
    It("意大利", "it"),
    /**
     * 日语
     */
    Jp("日语", "jp"),
    /**
     * 韩语
     */
    Kor("韩语", "kor"),
    /**
     * 尼德兰语
     */
    Nl("尼德兰", "nl"),
    /**
     * 波兰语
     */
    Pl("波兰", "pl"),
    /**
     * 葡萄牙语
     */
    Pt("葡萄牙", "pt"),
    /**
     * 罗马尼亚语
     */
    Rom("罗马尼亚", "rom"),
    /**
     * 俄语
     */
    Ru("俄语", "ru"),
    /**
     * 斯洛文尼亚语
     */
    Slo("斯洛文尼亚", "slo"),
    /**
     * 西班牙语
     */
    Spa("西班牙", "spa"),
    /**
     * 瑞典语
     */
    Swe("瑞典", "swe"),
    /**
     * 泰语
     */
    Th("泰语", "th"),
    /**
     * 越南语
     */
    Vie("越南", "vie"),
    /**
     * 文言文
     */
    Wyw("文言文", "wyw"),
    /**
     * 粤语
     */
    Yue("粤语", "yue"),
    /**
     * 中文
     */
    Zh("中文", "zh");

    /**
     * 中文名
     */
    public final String zh;
    /**
     * 语言缩写
     */
    public final String code;

    Languages(final String zh, final String code) {
        this.zh = zh;
        this.code = code;
    }

    /**
     * 根据语言缩写取枚举
     *
     * @param code 语言缩写
     * @return 匹配的枚举；或 Auto
     */
    public static @NotNull Languages get(@NotNull String code) {
        if (code.trim().isEmpty())
            return Auto;
        for (var lang : values()) {
            if (code.equals(lang.code))
                return lang;
        }
        return Auto;
    }
}
