package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.commands.core.CommandHandler;
import cz.ceph.lampcontrol.config.MainConfig;
import cz.ceph.lampcontrol.events.ReflectEvent;
import cz.ceph.lampcontrol.events.ReflectPlayerInteractEvent;
import cz.ceph.lampcontrol.listeners.LampListener;
import cz.ceph.lampcontrol.localization.Localization;
import cz.ceph.lampcontrol.workers.SwitchBlock;
import cz.ceph.lampcontrol.utils.VersionChecker;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LampControl extends JavaPlugin {

    private MainConfig mainConfig;
    private ReflectEvent reflectEvent;
    private SwitchBlock switchBlock;
    private CommandHandler commandHandler;

    public Map<String, Boolean> cachedBooleanValues;
    public List<Material> cachedRedstoneMaterials = new ArrayList<>();

    public Material lampTool;

    public static Localization localization;
    public static Logger debug = Logger.getLogger("LampControl");
    private static LampControl pluginMain;
    private static PluginDescriptionFile pluginInfo;
    public static String configLanguage;
    public static String bukkitVersion;

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {
        pluginMain = this;

        bukkitVersion = VersionChecker.getBukkitVersion();
        debug.info("Bukkit version is: " + bukkitVersion);


        debug.info("Initializing config file");
        cachedBooleanValues = new HashMap<>();

        mainConfig = new MainConfig(new File(getDataFolder(), "config.yml"));

        debug.info("Initializing default config values into cache");
        mainConfig.initializeConfig();

        debug.info("Initializing languages");
        localization = new Localization(this);
        localization.loadLocalization();
        debug.info("Available languages: " + localization.getAvailableLanguages().toString() + "");
        debug.info("Selected default language is: " + configLanguage + ", selected language is: " + Localization.resultLanguage);

        debug.info("Initializing CommandHandler");
        commandHandler = new CommandHandler(this);

        debug.info("Registering LampListener");
        LampListener lampListener = new LampListener();

        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));

        getServer().getPluginManager().registerEvents(lampListener, this);

        debug.info("Registering SwitchBlock function");
        switchBlock = new SwitchBlock();

        debug.info("Registering commands");
        commandHandler.loadCommands(LampControl.class);

        pluginInfo = this.getDescription();
        debug.info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was enabled!");



    }

    @Override
    public void onDisable() {
        debug.info("Unloading commands");
        commandHandler.unloadAll();
        getMainConfig().clearCachedValues();

        debug.info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was disabled!");
    }

    public static LampControl getMain() {
        return pluginMain;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public Localization getLocalization() {
        return localization;
    }

    public SwitchBlock getSwitchBlock() {
        return switchBlock;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public boolean containMaterials(Material mat) {
        return cachedRedstoneMaterials.contains(mat);
    }

}