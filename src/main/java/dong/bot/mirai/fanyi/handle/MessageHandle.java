package dong.bot.mirai.fanyi.handle;

import dong.bot.mirai.fanyi.FanYi;
import dong.bot.mirai.fanyi.Translator;
import dong.bot.mirai.fanyi.data.conf.PluginConfig;
import dong.bot.mirai.fanyi.enums.Languages;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dong.bot.mirai.fanyi.Const.*;

/**
 * 消息处理
 *
 * @author dong
 */
public abstract class MessageHandle {

    private static final MiraiLogger LOGGER = FanYi.LOGGER;

    /**
     * 缓存发送的最后一条消息链； key 是请求用户的 QQ 号
     */
    private static final Map<Long, MessageReceipt<?>> LAST_REPLIES = new ConcurrentHashMap<>(128);

    /**
     * 缓存曾经指定的语言； key 是请求用户的 QQ 号
     */
    private static final Map<Long, Languages> USED_LANGUAGES = new ConcurrentHashMap<>(128);

    /**
     * 文本回复
     *
     * @param event        上下文
     * @param context      文本内容
     * @param cacheReceipt 是否缓存回执
     */
    private static void replying(CharSequence context, boolean cacheReceipt, @NotNull MessageEvent event) {
        // quote 是指QQ中的“回复”。【回复+文本】属于一个消息链，需要用 plus 连接
        var msg = new QuoteReply(event.getMessage()).plus(context);
        var receipt = event.getSubject().sendMessage(msg);
        if (cacheReceipt) {
            // 缓存回执，可用于回复、撤回、溯源等场景
            LAST_REPLIES.put(event.getSender().getId(), receipt);
            LOGGER.info("lastReplies.count: " + LAST_REPLIES.size());
        }
    }

    /**
     * 发送提示文案
     *
     * @param event        上下文
     * @param cacheReceipt 是否缓存回执
     */
    public static void showTip(MessageEvent event, boolean cacheReceipt) {
        LOGGER.info("showLanguages");
        String tip = String.join("\n", PluginConfig.INSTANCE.getKeywordTips());
        replying(tip, cacheReceipt, event);
    }

    /**
     * 发送消息：语言列表
     *
     * @param event        上下文
     * @param cacheReceipt 是否缓存回执
     */
    public static void showLanguages(MessageEvent event, boolean cacheReceipt) {
        LOGGER.info("showLanguages");
        var ls = Languages.values();
        var txt = new StringBuilder(ls.length * 5 * 9);
        for (int x = 0; x < ls.length; x++) {
            // 两个一行（手机端屏幕窄）
            if (x > 0 && x % 2 < 1) txt.append('\n');
            var lang = ls[x];
            txt.append(String.format("%s %s、", lang.code, lang.zh));
        }
        replying(txt, cacheReceipt, event);
    }

    /**
     * 翻译
     * TODO 1.多 API
     * TODO 2.可自选 API
     *
     * @param event        上下文
     * @param cacheReceipt 是否缓存回执
     */
    public static void translate(@NotNull MessageEvent event, boolean cacheReceipt) {
        LOGGER.info("translate");
        var reqMsg = event.getMessage();
        var sender = event.getSender().getId();
        var args = reqMsg.contentToString().trim()
                .replaceAll(SPACES_REGEX, SPACE).split(SPACE, 3);
        // 检查参数合法
        if (args.length < 2) {
            String translateTip = PluginConfig.INSTANCE.getTranslateTip();
            replying(Tip.LACK_ARG + "\n" + translateTip, cacheReceipt, event);
            return;
        }

        String translation;
        String langDelimiter = PluginConfig.INSTANCE.getLangDelimiter();
        // 自动
        if (args.length == 2 || !args[1].contains(langDelimiter)) {
            if (USED_LANGUAGES.containsKey(sender)) {
                var to = USED_LANGUAGES.get(sender);
                translation = Translator.to(args[1], to);
            } else {
                translation = Translator.auto(args[1]);
            }
            replying(translation, cacheReceipt, event);
            return;
        }

        // 指定
        var lang = args[1].split(langDelimiter);
        Languages from = Languages.get(lang[0]);
        Languages to;
        if (lang.length > 1 && lang[1].length() > 0) {
            to = Languages.get(lang[1]);
        } else {
            to = Languages.Zh;
        }
        translation = Translator.to(args[2], from, to);
        replying(translation, cacheReceipt, event);

        if (USED_LANGUAGES.containsKey(sender) && to == Languages.Zh) {
            // 重置为默认的情况下移除缓存
            USED_LANGUAGES.remove(sender);
        } else if (!USED_LANGUAGES.containsKey(sender) && to != Languages.Zh) {
            // 缓存用户选择的语言
            USED_LANGUAGES.put(sender, to);
        }
    }
}
