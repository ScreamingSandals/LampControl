package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.config.MainConfig;
import cz.ceph.lampcontrol.core.Loader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LampControl extends JavaPlugin {
    private static LampControl lampControl;
    private Loader loader;
    private MainConfig mainConfig;

    private boolean isLegacy;
    private int versionNumber;
    private List<String> redstoneMaterials = new ArrayList<>();

    @Override
    public void onEnable() {
        lampControl = this;
        loader = new Loader(this);

        String[] bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        for (int i = 0; i < 2; i++) {
            versionNumber += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }

        isLegacy = versionNumber < 113;

        mainConfig = new MainConfig(this);
        redstoneMaterials = mainConfig.config.getStringList("redstone-materials");
    }

    @Override
    public void onDisable() {

    }

    public static LampControl getInstance() {
        return lampControl;
    }

    public Loader getLoader() {
        return loader;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public int getVersionNumber() {
        return versionNumber;
    }
}
