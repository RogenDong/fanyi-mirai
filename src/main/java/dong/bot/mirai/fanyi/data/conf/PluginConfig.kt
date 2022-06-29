package dong.bot.mirai.fanyi.data.conf

import dong.bot.mirai.fanyi.Const
import dong.bot.mirai.fanyi.Const.Keyword
import dong.bot.mirai.fanyi.Const.Tip
import dong.bot.mirai.fanyi.enums.Keywords
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 配置
 *
 * @author dong
 */
object PluginConfig : AutoSavePluginConfig("fanyi") {

    @ValueDescription("API 链接索引")
    var apis: MutableMap<String, String> by value()

    @ValueDescription("API AppId")
    var appIds: MutableMap<String, String> by value()

    @ValueDescription("API 密钥")
    var secretKeys: MutableMap<String, String> by value()

    /**
     * 语言参数的分隔符
     */
    @ValueDescription("语言参数的分隔符")
    var langDelimiter: String by value(Const.LANG_DELIMITER)

    /**
     * 关键词集合
     */
    @ValueDescription("配置关键词")
    var keywords: MutableMap<Keywords, String> by value(
        mutableMapOf(
            Keywords.Translate to Keyword.TRANS,
            Keywords.Languages to Keyword.LANG,
            Keywords.Tips to Keyword.TIP,
        )
    )

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

    /**
     * 配置项是否可用
     */
    fun unavailable() = apis.isEmpty() || appIds.isEmpty() || secretKeys.isEmpty()

    /**
     * 特定 API 的配置是否可用
     */
    fun available() = !unavailable()

    /**
     * 特定 API 的配置是否可用
     */
    fun unavailable(apiName: String) = unavailable() ||
        apis.containsKey(apiName) || appIds.containsKey(apiName) || secretKeys.containsKey(apiName)

    /**
     * 特定 API 的配置是否可用
     */
    fun available(apiName: String) = !unavailable(apiName)

}
