package online.afeibaili;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class Gobang extends JavaPlugin {
    public static final Gobang INSTANCE = new Gobang();

    private Gobang() {
        super(new JvmPluginDescriptionBuilder("online.afeibaili.gobang", "1.0.0")
                .name("Gobang")
                .info("五子棋图片化下棋")
                .author("AfeiBaili")
                .build());
    }

    @Override
    public void onEnable() {
        MessageHandle.load();
        getLogger().info("五子棋插件加载成功！");
    }
}