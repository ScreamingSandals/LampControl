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
    private static final int CONFIG_VERSION = 7;

    private static final String PATH_PLUGIN_PREFIX = "pluginPrefix";
    private static final String PATH_LAMPTOOL = "lampTool";
    private static final String PATH_USE_PERMISSIONS = "use.permissions";
    private static final String PATH_USE_ITEMS = "use.items";
    private static final String PATH_USE_PLATE_WOODEN = "use.plate.wooden";
    private static final String PATH_USE_PLATE_STONE = "use.plate.stone";
    private static final String PATH_LANGUAGE = "language";
    private static final String PATH_MANAGE_LAMPS = "manage.lamps";
    private static final String PATH_MANAGE_RAILS = "manage.rails";
    private static final String PATH_MANAGE_OP = "manage.op";

    public MainConfig(File file) {
        super(file, CONFIG_VERSION);
    }

    @Override
    public void setDefault() {
        setString(PATH_PLUGIN_PREFIX, "&8[&eLamp&cControl&8]&r");
        setString(PATH_LAMPTOOL, "FLINT_AND_STEEL");
        setBoolean(PATH_USE_PERMISSIONS, true);
        setBoolean(PATH_USE_ITEMS, false);
        setBoolean(PATH_USE_PLATE_WOODEN, true);
        setBoolean(PATH_USE_PLATE_STONE, true);
        setString(PATH_LANGUAGE, "en");
        setBoolean(PATH_MANAGE_LAMPS, true);
        setBoolean(PATH_MANAGE_RAILS, true);
        setBoolean(PATH_MANAGE_OP, true);
        setInt(FILE_VERSION_PATH, 7);
    }

    public String getPluginPrefix() {
        return getString(PATH_PLUGIN_PREFIX);
    }

    public void initializeConfig() {
        getMain().lampTool = Material.getMaterial(getString(PATH_LAMPTOOL));
        getMain().cachedBooleanValues.put("use-permissions", getBoolean(PATH_USE_PERMISSIONS));
        getMain().cachedBooleanValues.put("use-items", getBoolean(PATH_USE_ITEMS));
        getMain().cachedBooleanValues.put("use-plate-wooden", getBoolean(PATH_USE_PLATE_WOODEN));
        getMain().cachedBooleanValues.put("use-plate-stone", getBoolean(PATH_USE_PLATE_STONE));
        getMain().cachedBooleanValues.put("manage-lamps", getBoolean(PATH_MANAGE_LAMPS));
        getMain().cachedBooleanValues.put("manage-rails", getBoolean(PATH_MANAGE_RAILS));
        getMain().cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));
        getMain().cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));
        getMain().language = getString(PATH_LANGUAGE);

        //if (!areMaterialsConfigured()) {
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
            if (getMain().cachedBooleanValues.get("use-plate-wooden"))
                getMain().cachedRedstoneMaterials.add(Material.WOOD_PLATE);
            if (getMain().cachedBooleanValues.get("use-plate-stone"))
                getMain().cachedRedstoneMaterials.add(Material.STONE_PLATE);
        //} else {
            //TODO: add loading from config
        //}
    }

    //private boolean areMaterialsConfigured() {
        //return getBoolean("materials.custom");
    }

