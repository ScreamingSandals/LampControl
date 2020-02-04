package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.config.BaseConfig;
import cz.ceph.lampcontrol.config.ConfigInstance;
import cz.ceph.lampcontrol.environment.Environment;
import cz.ceph.lampcontrol.environment.FlatteningEnvironment;
import cz.ceph.lampcontrol.environment.LegacyEnvironment;
import cz.ceph.lampcontrol.environment.MainEnvironment;
import cz.ceph.lampcontrol.listeners.PlayerListener;
import cz.ceph.lampcontrol.world.SwitchBlock;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.lang.Language;
import org.screamingsandals.lib.screamingcommands.ScreamingCommands;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    @Getter
    private static BaseConfig baseConfig;
    @Getter
    private static Environment environment;
    private ScreamingCommands screamingCommands;
    @Getter
    private static cz.ceph.lampcontrol.api.SwitchBlock switchBlock = new SwitchBlock();

    @Override
    public void onEnable() {
        instance = this;

        PaperLib.suggestPaper(this);
        if (PaperLib.isVersion(12)) {
            environment = new LegacyEnvironment(this);
        } else if (PaperLib.isVersion(13)) {
            environment = new FlatteningEnvironment(this);
        } else {
            environment = new MainEnvironment(this);
        }

        baseConfig = new ConfigInstance(createConfigFile(getDataFolder(), "config.yml"));
        baseConfig.initialize();
        baseConfig.checkDefaultValues();

        screamingCommands = new ScreamingCommands(this, Main.class, "cz/ceph/lampcontrol");
        screamingCommands.loadCommands();

        Language language = new Language(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        screamingCommands.unloadCommands();

        getServer().getServicesManager().unregisterAll(this);
    }

    private File createConfigFile(File dataFolder, String fileName) {
        dataFolder.mkdirs();

        File configFile = new File(dataFolder, fileName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return configFile;
    }
}
