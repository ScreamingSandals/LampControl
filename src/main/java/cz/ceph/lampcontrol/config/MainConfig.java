package cz.ceph.lampcontrol.config;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainConfig {
    public File configFile;
    public FileConfiguration config;

    public final File dataFolder;
    public final LampControl lampControl;

    public MainConfig(LampControl lampControl) {
        this.dataFolder = lampControl.getDataFolder();
        this.lampControl = lampControl;
    }

    public boolean createFiles() {
        configFile = new File(dataFolder, "config.yml");
        config = new YamlConfiguration();

        if (!configFile.exists()) {
            lampControl.saveResource("config.yml", false);
        }
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        AtomicBoolean modify = new AtomicBoolean(false);
        checkOrSetConfig(modify, "prefix", "&8[&eLamp&cControl&8]&r");
        checkOrSetConfig(modify, "locale", "en");
        checkOrSetConfig(modify, "debug", false);
        checkOrSetConfig(modify, "redstone-materials", new ArrayList<String>() {
            {
                add("/aac");
                add("/playerbalancer");
                add("/hd");
                add("/holo");
                add("/holograms");
                add("/hologram");
                add("/worldedit");
                add("//sel");
                add("/titlemanager");
                add("/tm");
                add("/npcmd");
                add("/multiworld");
            }
        });

        if (modify.get()) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void checkOrSetConfig(AtomicBoolean modify, String path, java.io.Serializable value) {
        checkOrSet(modify, this.config, path, value);
    }

    private static void checkOrSet(AtomicBoolean modify, FileConfiguration config, String path, java.io.Serializable value) {
        if (!config.isSet(path)) {
            config.set(path, value);
            modify.set(true);
        }
    }
}
