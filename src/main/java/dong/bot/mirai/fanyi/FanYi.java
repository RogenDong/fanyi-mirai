package dong.bot.mirai.fanyi;

import dong.bot.mirai.fanyi.data.conf.PluginConfig;
import dong.bot.mirai.fanyi.listen.MessageListener;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

/**
 * @author dong
 */
public final class FanYi extends JavaPlugin {

    public static final FanYi INSTANCE = new FanYi();
    public static final MiraiLogger LOGGER = INSTANCE.getLogger();

    private FanYi() {
        super(new JvmPluginDescriptionBuilder("dong.bot.mirai.fanyi", "0.1")
                .author("dong").info("翻译 bot").build());
    }


    @Override
    public void onDisable() {
        LOGGER.info("关闭插件……");
    }

    @Override
    public void onEnable() {
        LOGGER.info("插件已启用，开始倾听");
        // 启用消息倾听
        GlobalEventChannel.INSTANCE.registerListenerHost(new MessageListener());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage plugin) {
        this.reloadPluginConfig(PluginConfig.INSTANCE);
    }
}
