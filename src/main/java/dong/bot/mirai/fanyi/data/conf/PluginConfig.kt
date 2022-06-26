package dong.bot.mirai.fanyi.data.conf

import dong.bot.mirai.fanyi.Const
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import dong.bot.mirai.fanyi.enums.Keywords

import dong.bot.mirai.fanyi.Const.Keyword
import dong.bot.mirai.fanyi.Const.Tip

/**
 * 配置
 *
 * @author dong
 */
object PluginConfig : AutoSavePluginConfig("fanyi") {

    @ValueDescription("百度 API AppId")
    var baiduAppId: String by value()

    @ValueDescription("百度 API 密钥")
    var baiduSecretKey: String by value()

    /**
     * 语言参数的分隔符
     */
    @ValueDescription("语言参数的分隔符")
    var langDelimiter: String by value(Const.LANG_DELIMITER)

    /**
     * 关键词集合
     */
    @ValueDescription("配置关键词")
    var keywords: MutableMap<Keywords, String> by value(mutableMapOf(
        Keywords.Translate to Keyword.TRANS,
        Keywords.Languages to Keyword.LANG,
        Keywords.Tips to Keyword.TIP,
    ))

    /**
     * 翻译的用法说明
     */
    @ValueDescription("翻译的用法说明")
    var translateTip: String by value(Tip.TRANS)

    /**
     * 列出关键词及说明
     */
    @ValueDescription("列出关键词及说明")
    var keywordTips: List<String> by value(Tip.KEYWORD.split('\n'))
}
