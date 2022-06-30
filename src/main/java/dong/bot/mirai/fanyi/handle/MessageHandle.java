package dong.bot.mirai.fanyi.handle;

import dong.bot.mirai.fanyi.FanYi;
import dong.bot.mirai.fanyi.Translator;
import dong.bot.mirai.fanyi.data.conf.PluginConfig;
import dong.bot.mirai.fanyi.enums.Keywords;
import dong.bot.mirai.fanyi.enums.Languages;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
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
     * 可用的翻译 API
     */
    private static final Map<String, Translator> TRANS_API = new ConcurrentHashMap<>(16);

    /**
     * 添加翻译 API
     *
     * @param translator API 实例
     */
    public static void addTranslator(@NotNull Translator translator) {
        translator.getName().ifPresent(name -> addTranslator(name, translator));
    }

    /**
     * 添加翻译 API
     *
     * @param name       API 名称
     * @param translator API 实例
     */
    public static void addTranslator(@NotNull String name, @NotNull Translator translator) {
        if (TRANS_API.containsKey(name))
            return;
        TRANS_API.put(name, translator);
        LOGGER.info("add API: " + name);
    }

    /**
     * 文本回复
     *
     * @param event        上下文
     * @param context      文本内容
     * @param cacheReceipt 是否缓存回执
     */
    private static void replying(CharSequence context, boolean cacheReceipt, @NotNull MessageEvent event) {
        // quote 是指QQ 中的“回复”。【回复+文本】属于一个消息链，需要用 plus 连接
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
     * 提示缺少参数
     *
     * @param event        上下文
     * @param cacheReceipt 是否缓存回执
     */
    public static void lackArgs(MessageEvent event, boolean cacheReceipt) {
        String translateTip = PluginConfig.INSTANCE.getTranslateTip();
        replying(Tip.LACK_ARG + "\n" + translateTip, cacheReceipt, event);
    }

    /**
     * 自动选择语言
     *
     * @param args   参数
     * @param sender 查询者 id
     * @param api    接口实例
     * @return 译文
     */
    private static Optional<String> auto(String[] args, long sender, Translator api) {
        var inp = String.join(SPACE, args);
        // TODO 检测到关键词时才复用指定语言
        if (USED_LANGUAGES.containsKey(sender)) {
            var to = USED_LANGUAGES.get(sender);
            return api.to(inp, Languages.Auto, to);
        } else {
            return api.auto(inp);
        }
    }

    /**
     * 指定语言
     *
     * @param args   参数
     * @param sender 查询者 id
     * @param api    接口实例
     * @return 译文
     */
    private static Optional<String> appoint(String[] args, long sender, Translator api) {
        var arg1 = args[0];
        var lang = arg1.split('[' + LANG_DELIMITER + ']');
        Languages from;
        Languages to;
        if (lang.length < 2) {
            from = Languages.Auto;
            to = Languages.Zh;
        } else {
            from = Languages.get(lang[0]);
            to = (lang[1].length() > 0) ? Languages.get(lang[1]) : Languages.Zh;
        }
        var ori = new StringBuilder(api.getOriginalMaxLength());
        for (int x = 1; x < args.length; x++) {
            ori.append(args[x]);
            ori.append(SPACE);
        }
        Optional<String> translation = api.to(ori.toString(), from, to);
        translation.ifPresent(t -> {
            if (to == Languages.Zh)
                USED_LANGUAGES.remove(sender);// 移除缓存
            else USED_LANGUAGES.put(sender, to);// 缓存用户选择的语言
        });
        return translation;
    }

    /**
     * 翻译
     *
     * @param event        上下文
     * @param cacheReceipt 是否缓存回执
     */
    public static void translate(@NotNull MessageEvent event, boolean cacheReceipt) {
        MessageChain reqMsg = event.getMessage();
        long sender = event.getSender().getId();
        String[] args = reqMsg.contentToString()
                .substring(Keywords.Translate.getWord().length() + 1).trim()
                .replaceAll(SPACES_REGEX, SPACE).split(SPACE, 3);

        // 检查参数合法
        if (args.length < 1) {
            lackArgs(event, cacheReceipt);
            return;
        }

        // TODO 可自选 API
        String apiName = "baidu";
        Translator api = TRANS_API.get(apiName);
        if (api == null) {
            LOGGER.warning(String.format("API 实例【%s】未定义！", apiName));
            return;
        }

        String arg1 = args[0];
        Optional<String> translation = arg1.contains(LANG_DELIMITER) ?
                appoint(args, sender, api) :
                auto(args, sender, api);
        translation.ifPresent(t -> replying(t, cacheReceipt, event));
    }
}
