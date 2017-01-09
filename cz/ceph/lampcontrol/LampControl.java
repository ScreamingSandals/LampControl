package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.commands.Commands;
import cz.ceph.lampcontrol.config.MainConfig;
import cz.ceph.lampcontrol.events.ReflectEvent;
import cz.ceph.lampcontrol.events.ReflectPlayerInteractEvent;
import cz.ceph.lampcontrol.listeners.LampListener;
import cz.ceph.lampcontrol.localization.Localizations;
import cz.ceph.lampcontrol.utils.StringUtils;
import cz.ceph.lampcontrol.utils.SwitchBlock;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LampControl extends JavaPlugin {

    private MainConfig mainConfig;
    private ReflectEvent reflectEvent;
    private SwitchBlock switchBlock;
    private Localizations localizations;

    public Map<String, Boolean> cachedBooleanValues;
    public List<Material> cachedRedstoneMaterials = new ArrayList<>();

    public Material lampTool;
    public String language;

    private static LampControl pluginMain;
    public static Logger debug = Logger.getLogger("Minecraft");
    public static PluginDescriptionFile pluginInfo;

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {

        debug.log(Level.INFO, "Initializing config file");
        mainConfig = new MainConfig(new File(getDataFolder(), "config.yml"));

        debug.log(Level.INFO, "Initializing default config values into cache");
        mainConfig.initializeConfig();

        debug.log(Level.INFO, "Initializing languages");
        localizations = new Localizations(this);
        localizations.findAndLoadFiles();
        debug.log(Level.INFO, "Available languages: [{0}]", StringUtils.arrToString(", ", localizations.getAvailableLanguages()));

        debug.log(Level.INFO, "Registering LampListener");
        LampListener lampListener = new LampListener(this);

        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));

        getServer().getPluginManager().registerEvents(lampListener, this);

        debug.log(Level.INFO, "Registering SwitchBlock function");
        switchBlock = new SwitchBlock();

        debug.log(Level.INFO, "Registering commands");
        Commands commandExecutor = new Commands(this);
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        pluginMain = this;

        pluginInfo = this.getDescription();
        debug.log(Level.INFO, pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was enabled!");
    }

    @Override
    public void onDisable() {
        debug.log(Level.INFO, pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was disabled!");
    }


    public static LampControl getMain() {
        return pluginMain;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public Localizations getLocalizations() {
        return localizations;
    }

    public SwitchBlock getSwitchBlock() {
        return switchBlock;
    }

    public boolean containMaterials(Material mat) {
        return cachedRedstoneMaterials.contains(mat);
    }
}