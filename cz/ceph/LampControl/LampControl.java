/*
	Code has been adapted from jacklink01.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.lampcontrol;

import cz.ceph.lampcontrol.config.MainConfig;
import cz.ceph.lampcontrol.events.ReflectEvent;
import cz.ceph.lampcontrol.events.ReflectPlayerInteractEvent;
import cz.ceph.lampcontrol.listeners.LampListener;
import cz.ceph.lampcontrol.utils.Commands;
import cz.ceph.lampcontrol.utils.SwitchBlock;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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

    public Map<String, Boolean> cachedBooleanValues;
    public List<Material> cachedRedstoneMaterials = new ArrayList<>();

    public Material lampTool;

    private static LampControl pluginMain;
    public static Logger debug = Logger.getLogger("Minecraft");

    //old things
    public static PluginDescriptionFile pluginInfo;
    public static YamlConfiguration messagesConfig;

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {

        debug.log(Level.SEVERE, "Loading config.yml");
        mainConfig = new MainConfig(new File(getDataFolder(), "config.yml"));

        debug.log(Level.SEVERE, "Loading default config values into cache");
        mainConfig.initializeConfig();

        debug.log(Level.SEVERE, "Registering LampListener");
        LampListener lampListener = new LampListener(this);

        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));

        getServer().getPluginManager().registerEvents(lampListener, this);

        debug.log(Level.SEVERE, "Registering SwitchBlock function");
        switchBlock = new SwitchBlock();

        debug.log(Level.SEVERE, "Registering commands");
        Commands commandExecutor = new Commands(this);
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        pluginMain = this;

        // old things
        loadMessages();

        pluginInfo = this.getDescription();
        debug.log(Level.SEVERE, pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was disabled!");
    }


    public static LampControl getMain() {
        return pluginMain;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    // Load messages from the file
    private void loadMessages() {
        File messages = new File(getDataFolder(), "messages.yml");
        if (!messages.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messages);
    }

    public boolean containMaterials(Material mat) {
        return cachedRedstoneMaterials.contains(mat);
    }

    public SwitchBlock getSwitchBlock() {
        return switchBlock;
    }
}