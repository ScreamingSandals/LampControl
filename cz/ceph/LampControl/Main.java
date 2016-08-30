/*
	Code has been adapted from jacklink01.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.LampControl;

import cz.ceph.LampControl.listeners.LampListener;
import cz.ceph.LampControl.listeners.ReflectPlayerInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {
    public static boolean opUsesHand = true, toggleLamps = true, takeItemOnUse = false, usePermissions = false, woodPlateControl = false, stonePlateControl = false, controlRails = true;
    public static PluginDescriptionFile pluginInfo;
    public static File MessagesFile;
    public static YamlConfiguration YamlConfig;
    private static final int CONFIG_VERSION = 5;

    public Material toolMaterial;
    private ReflectEvent reflectEvent;
    private List<Material> redstone_materials = new ArrayList<>();


    @Override
    public void onDisable() {
        // Output info to console on disable
        getLogger().info("Disabled LampControl! Don't worry, I'll be back. Muhaha");
    }

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {
        // Create default config if not exist yet.
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        // Load configuration.
        loadConfig();

        // Load messages
        loadMessages();

        // Check for old config
        if (!getConfig().isSet("config-version") || getConfig().getInt("config-version") < CONFIG_VERSION) {
            File file = new File(this.getDataFolder(), "config.yml");
            file.delete();
            saveDefaultConfig();
            getLogger().info("Older config version found, created new one.");
        }

        // Register listeners
        LampListener lampListener = new LampListener(this);
        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));
        getServer().getPluginManager().registerEvents(lampListener, this);

        // Register command executors
        Commands commandExecutor = new Commands();
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        // Output info to console on load
        pluginInfo = this.getDescription();
        getLogger().info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " is enabled!");

        this.redstone_materials.add(Material.DETECTOR_RAIL);
        this.redstone_materials.add(Material.POWERED_RAIL);
        this.redstone_materials.add(Material.REDSTONE_WIRE);
        this.redstone_materials.add(Material.REDSTONE_BLOCK);
        this.redstone_materials.add(Material.PISTON_MOVING_PIECE);
        this.redstone_materials.add(Material.REDSTONE_TORCH_OFF);
        this.redstone_materials.add(Material.REDSTONE_TORCH_ON);
        this.redstone_materials.add(Material.DIODE_BLOCK_OFF);
        this.redstone_materials.add(Material.DIODE_BLOCK_ON);
        this.redstone_materials.add(Material.REDSTONE_COMPARATOR_OFF);
        this.redstone_materials.add(Material.REDSTONE_COMPARATOR_ON);
        this.redstone_materials.add(Material.DIODE_BLOCK_ON);
        this.redstone_materials.add(Material.LEVER);
        this.redstone_materials.add(Material.STONE_BUTTON);
        this.redstone_materials.add(Material.WOOD_BUTTON);
        this.redstone_materials.add(Material.GOLD_PLATE);
        this.redstone_materials.add(Material.IRON_PLATE);
        this.redstone_materials.add(Material.TRIPWIRE);
        this.redstone_materials.add(Material.TRIPWIRE_HOOK);
        this.redstone_materials.addAll(Arrays.stream(Material.values()).filter(mat -> mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR") || mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR_INVERTED")).collect(Collectors.toList()));
        if (this.stonePlateControl)
            this.redstone_materials.add(Material.WOOD_PLATE);
        if (this.woodPlateControl)
            this.redstone_materials.add(Material.STONE_PLATE);
    }

    // Load config from the file
    @SuppressWarnings("deprecation")
    private void loadConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        toolMaterial = Material.getMaterial(getConfig().getInt("lampControlItem"));
        usePermissions = getConfig().getBoolean("usePermissions");
        woodPlateControl = getConfig().getBoolean("woodPlateControl");
        stonePlateControl = getConfig().getBoolean("stonePlateControl");
        opUsesHand = getConfig().getBoolean("opUsesHand");
        takeItemOnUse = getConfig().getBoolean("takeItemOnUse");
        toggleLamps = getConfig().getBoolean("toggleLamps");
        controlRails = getConfig().getBoolean("controlRails");
    }

    // Load messages from the file
    private void loadMessages() {
        File messages = new File(getDataFolder(), "messages.yml");
        if (!messages.exists()) {
            saveResource("messages.yml", false);
        }
        YamlConfig = YamlConfiguration.loadConfiguration(messages);
        MessagesManager.setFile(YamlConfig);
        MessagesFile = messages;
    }

    public boolean isInRedstoneMaterials(Material mat) {
        return redstone_materials.contains(mat);
    }

    // Get what version of Spigot we're using
    public static String getNMSVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}