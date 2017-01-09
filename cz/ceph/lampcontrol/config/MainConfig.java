package cz.ceph.lampcontrol.config;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 08.01.2017.
 */

public class MainConfig extends BaseConfiguration {
    private static final int CONFIG_VERSION = 5;

    private static final String PATH_PLUGIN_PREFIX = "pluginPrefix";
    private static final String PATH_PERMISSIONS = "permissions";

    public MainConfig(File file) {
        super(file, CONFIG_VERSION);
    }

    @Override
    public void setDefault() {
        setString(PATH_PLUGIN_PREFIX, "&8[&eLamp&cControl&8]&r");
    }

    public String getCustomString(String name) {
        return getString(name);
    }

    public String getPluginPrefix() {
        return getString(PATH_PLUGIN_PREFIX);
    }

    public void initializeConfig() {

        LampControl.debug.log(Level.INFO, "Loading RedstoneMaterials into cache.");
        if (!areMaterialsConfigured()) {
            getMain().cachedRedstoneMaterials.add(Material.DETECTOR_RAIL);
            getMain().cachedRedstoneMaterials.add(Material.POWERED_RAIL);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_WIRE);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_BLOCK);
            getMain().cachedRedstoneMaterials.add(Material.PISTON_MOVING_PIECE);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_TORCH_OFF);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_TORCH_ON);
            getMain().cachedRedstoneMaterials.add(Material.DIODE_BLOCK_OFF);
            getMain().cachedRedstoneMaterials.add(Material.DIODE_BLOCK_ON);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_COMPARATOR_OFF);
            getMain().cachedRedstoneMaterials.add(Material.REDSTONE_COMPARATOR_ON);
            getMain().cachedRedstoneMaterials.add(Material.DIODE_BLOCK_ON);
            getMain().cachedRedstoneMaterials.add(Material.LEVER);
            getMain().cachedRedstoneMaterials.add(Material.STONE_BUTTON);
            getMain().cachedRedstoneMaterials.add(Material.WOOD_BUTTON);
            getMain().cachedRedstoneMaterials.add(Material.GOLD_PLATE);
            getMain().cachedRedstoneMaterials.add(Material.IRON_PLATE);
            getMain().cachedRedstoneMaterials.add(Material.TRIPWIRE);
            getMain().cachedRedstoneMaterials.add(Material.TRIPWIRE_HOOK);
            getMain().cachedRedstoneMaterials.addAll(Arrays.stream(Material.values()).filter(mat -> mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR") || mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR_INVERTED")).collect(Collectors.toList()));
            if (getMain().cachedBooleanValues.get("woodenPlateControl"))
                getMain().cachedRedstoneMaterials.add(Material.WOOD_PLATE);
            if (getMain().cachedBooleanValues.get("stonePlateControl"))
                getMain().cachedRedstoneMaterials.add(Material.STONE_PLATE);
        } else {
            //TODO: add loading from config
        }

        getMain().lampTool = Material.getMaterial(getString("lampTool"));
        getMain().cachedBooleanValues.put(PATH_PERMISSIONS, getBoolean(PATH_PERMISSIONS));

        /*
        woodPlateControl = getConfig().getBoolean("woodPlateControl");
        stonePlateControl = getConfig().getBoolean("stonePlateControl");
        opUsesHand = getConfig().getBoolean("opUsesHand");
        takeItemOnUse = getConfig().getBoolean("takeItemOnUse");
        toggleLamps = getConfig().getBoolean("toggleLamps");
        controlRails = getConfig().getBoolean("controlRails");
        */

    }

    private boolean areMaterialsConfigured() {
        return getBoolean("materials.custom");
    }


}
