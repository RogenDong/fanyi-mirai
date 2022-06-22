package dong.bot.mirai.fanyi.listen;

import dong.bot.mirai.fanyi.FanYi;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import static dong.bot.mirai.fanyi.Const.Keyword.*;
import static dong.bot.mirai.fanyi.handle.MessageHandle.*;

/**
 * 倾听消息
 *
 * @author dong
 */
public class MessageListener extends SimpleListenerHost {

    private static final MiraiLogger LOGGER = FanYi.LOGGER;

    /**
     * 群消息
     *
     * @param event 事件
     */
    @EventHandler
    public void OnGroup(@NotNull GroupMessageEvent event) {
        LOGGER.info("GroupMessageEvent");
        callByKeyword(event, true);
    }

    /**
     * 好友私聊
     *
     * @param event 事件
     */
    @EventHandler
    public void OnFriend(@NotNull FriendMessageEvent event) {
        LOGGER.info("FriendMessageEvent");
        callByKeyword(event, false);
    }

    /**
     * 捕获未处理的异常
     *
     * @param context   协程上下文
     * @param exception 异常信息
     */
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LOGGER.warning("MessageListener - 未处理的异常", exception);
    }

    /**
     * 根据消息中的关键词来调用功能
     *
     * @param event        消息
     * @param cacheReceipt 是否缓存回执
     */
    private void callByKeyword(@NotNull MessageEvent event, boolean cacheReceipt) {
        var msg = event.getMessage();
        var inp = msg.contentToString();
        switch (inp) {
            case HELP -> showTip(event, cacheReceipt);
            case LANG -> showLanguages(event, cacheReceipt);
            default -> {
                if (inp.startsWith(TRANS) && inp.length() > TRANS.length())
                    translate(event, cacheReceipt);
            }
        }
    }
}
