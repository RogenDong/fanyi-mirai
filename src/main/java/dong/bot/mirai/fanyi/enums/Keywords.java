package dong.bot.mirai.fanyi.enums;

import dong.bot.mirai.fanyi.data.conf.PluginConfig;

/**
 * 关键词枚举
 *
 * @author dong
 */
public enum Keywords {
    Translate,
    Languages,
    Tips;

    public String getWord() {
        return PluginConfig.INSTANCE.getKeywords().get(this);
    }

    /**
     * 检查枚举与输入的值否匹配
     *
     * @param input 输入
     * @return 是否匹配
     */
    public boolean equals(String input) {
        return this.getWord().equals(input);
    }
}
