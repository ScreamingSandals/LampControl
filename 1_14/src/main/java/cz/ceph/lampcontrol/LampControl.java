package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.commands.core.CommandHandler;
import cz.ceph.lampcontrol.config.MainConfig;
import cz.ceph.lampcontrol.events.ReflectEvent;
import cz.ceph.lampcontrol.events.ReflectPlayerInteractEvent;
import cz.ceph.lampcontrol.listeners.LampListener;
import cz.ceph.lampcontrol.localization.Localization;
import cz.ceph.lampcontrol.utils.VersionChecker;
import cz.ceph.lampcontrol.workers.SwitchBlock;
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

    private PluginDescriptionFile pluginInfo;
    public String configLanguage;
    public String bukkitVersion;

    public Map<String, Boolean> cachedBooleanValues;
    public List<Material> cachedRedstoneMaterials = new ArrayList<>();

    public Material lampTool;

    public static Localization localization;
    public static Logger debug = Logger.getLogger("LampControl");
    private static LampControl pluginMain;

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {
        pluginMain = this;

        bukkitVersion = VersionChecker.getBukkitVersion();
        //debug.info("Bukkit version is: " + bukkitVersion);

        debug.info("Initializing config file");
        cachedBooleanValues = new HashMap<>();
        configLoad();

        debug.info("Initializing languages");
        localization = new Localization(this);
        localization.loadLocalization();
        debug.info("Available languages: " + localization.getAvailableLanguages().toString() + "");
        debug.info("Using language: " + localization.resultLanguage);

        debug.info("Initializing Core");
        commandHandler = new CommandHandler(this);

        //debug.info("Registering LampListener");
        LampListener lampListener = new LampListener();
        getServer().getPluginManager().registerEvents(lampListener, this);

        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));


        //debug.info("Registering SwitchBlock function");
        switchBlock = new SwitchBlock();

        //debug.info("Registering commands");
        commandHandler.loadCommands(LampControl.class);

        pluginInfo = this.getDescription();
        debug.info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was enabled successfully!");



    }

    @Override
    public void onDisable() {
        debug.info("Unloading Core");
        commandHandler.unloadAll();
        getMainConfig().clearCachedValues();

        debug.info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was disabled :(");
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

    public void configLoad() {
        mainConfig = new MainConfig(new File(getDataFolder(), "config.yml"));
        mainConfig.initializeConfig();
    }

}