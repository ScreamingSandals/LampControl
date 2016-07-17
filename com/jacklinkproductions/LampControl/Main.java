package com.jacklinkproductions.LampControl;

import com.jacklinkproductions.LampControl.listeners.LampListener;
import com.jacklinkproductions.LampControl.listeners.ReflectPlayerInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static final String prefix = "[LampControl] ";
    public static boolean opUsesHand = true, toggleLamps = true, takeItemOnUse = false, usePermissions = false;
    public static PluginDescriptionFile pdfFile;
    public static final int CONFIG_VERSION = 4;

    public Material toolMaterial;
    private ReflectEvent reflectEvent;
    private List<Material> redstone_materials = new ArrayList<>();
    private boolean woodPlateControl = false, stonePlateControl = false;


    @Override
    public void onDisable() {
        // Output info to console on disable
        getLogger().info("Thanks for using LampControl!");
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
        reloadConfiguration();

        // Check for old config
        if (!getConfig().isSet("config-version") || getConfig().getInt("config-version") < CONFIG_VERSION) {
            File file = new File(this.getDataFolder(), "config.yml");
            file.delete();
            saveDefaultConfig();
            getLogger().info("Created a new config.yml for this version.");
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
        pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " is enabled!");

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
        this.redstone_materials.add(Material.DAYLIGHT_DETECTOR);
        this.redstone_materials.add(Material.DAYLIGHT_DETECTOR_INVERTED);
        if (this.stonePlateControl)
            this.redstone_materials.add(Material.WOOD_PLATE);
        if (this.woodPlateControl)
            this.redstone_materials.add(Material.STONE_PLATE);
    }

    @SuppressWarnings("deprecation")
    public void reloadConfiguration() {
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
    }

    public boolean isInRedstoneMaterials(Material mat) {
        return redstone_materials.contains(mat);
    }

    public static String getNMSVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}