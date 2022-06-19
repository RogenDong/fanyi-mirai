package dong.bot.mirai.fanyi;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
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
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage plugin) {
    }
}
