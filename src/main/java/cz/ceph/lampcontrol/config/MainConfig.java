package cz.ceph.lampcontrol.config;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 08.01.2017.
 */

public class MainConfig extends BaseConfiguration {
    private static final int CONFIG_VERSION = 9;

    private static final String PATH_PLUGIN_PREFIX = "pluginPrefix";
    private static final String PATH_LAMPTOOL = "lampTool";
    private static final String PATH_USE_PERMISSIONS = "use.permissions";
    private static final String PATH_USE_ITEMS = "use.items";
    private static final String PATH_USE_PLATES = "use.plates";
    private static final String PATH_LANGUAGE = "language";
    private static final String PATH_MANAGE_LAMPS = "manage.lamps";
    private static final String PATH_MANAGE_RAILS = "manage.rails";
    private static final String PATH_MANAGE_OP = "manage.op";
    private static final String PATH_MATERIALS_CONFIGURED = "materials.configured";
    private static final String PATH_MATERIALS_USEITEM = "materials.useItem";
    private static final String PATH_MATERIALS_LIST = "materials.customMaterials";
    private static final String PATH_SOUND_SUCCESS = "sound.success";
    private static final String PATH_SOUND_FAIL = "sound.fail";
    private static final String PATH_SOUND_DEFAULT = "sound.default";
    private boolean isConfigInitialized = false;

    private List<Material> cachedRedstoneMaterials = getMain().cachedRedstoneMaterials;
    private Map<String, Boolean> cachedBooleanValues = getMain().cachedBooleanValues;

    public MainConfig(File file) {
        super(file, CONFIG_VERSION);
    }

    @Override
    public void setDefault() {
        List<String> materialsConfigDefault = new ArrayList<>();
        materialsConfigDefault.add("add");
        materialsConfigDefault.add("materials");
        materialsConfigDefault.add("here");

        setString(PATH_PLUGIN_PREFIX, "&8[&eLamp&cControl&8]&r");
        setString(PATH_LAMPTOOL, "FLINT_AND_STEEL");
        setBoolean(PATH_USE_PERMISSIONS, true);
        setBoolean(PATH_USE_ITEMS, false);
        setBoolean(PATH_USE_PLATES, true);
        setString(PATH_LANGUAGE, "en");
        setBoolean(PATH_MANAGE_LAMPS, true);
        setBoolean(PATH_MANAGE_RAILS, true);
        setBoolean(PATH_MANAGE_OP, true);
        setBoolean(PATH_MATERIALS_CONFIGURED, false);
        setList(PATH_MATERIALS_LIST, materialsConfigDefault);
        setString(PATH_SOUND_DEFAULT, "CLICK");
        setString(PATH_SOUND_SUCCESS, "succ");
        setString(PATH_SOUND_FAIL, "fail");
        setInt(FILE_VERSION_PATH, 9);
    }

    public String getPluginPrefix() {
        return getString(PATH_PLUGIN_PREFIX);
    }

    public void initializeConfig() {
        if (isConfigInitialized) {
            clearCachedValues();
        }

        getMain().lampTool = Material.getMaterial(getString(PATH_LAMPTOOL));
        cachedBooleanValues.put("use-permissions", getBoolean(PATH_USE_PERMISSIONS));
        cachedBooleanValues.put("use-items", getBoolean(PATH_USE_ITEMS));
        cachedBooleanValues.put("use-plates", getBoolean(PATH_USE_PLATES));
        cachedBooleanValues.put("manage-lamps", getBoolean(PATH_MANAGE_LAMPS));
        cachedBooleanValues.put("manage-rails", getBoolean(PATH_MANAGE_RAILS));
        cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));
        cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));

        LampControl.language = getString(PATH_LANGUAGE);
        isConfigInitialized = true;

        if (cachedBooleanValues.get("use-items")) {
            setString(PATH_MATERIALS_USEITEM, "FLINT_AND_STEEL");
        }

        if (!areMaterialsConfigured()) {
            cachedRedstoneMaterials.add(Material.PISTON);
            cachedRedstoneMaterials.add(Material.MOVING_PISTON);
            cachedRedstoneMaterials.add(Material.PISTON_HEAD);
            cachedRedstoneMaterials.add(Material.STICKY_PISTON);
            cachedRedstoneMaterials.add(Material.COMPARATOR);
            cachedRedstoneMaterials.add(Material.BIRCH_BUTTON);
            cachedRedstoneMaterials.add(Material.ACACIA_BUTTON);
            cachedRedstoneMaterials.add(Material.DARK_OAK_BUTTON);
            cachedRedstoneMaterials.add(Material.JUNGLE_BUTTON);
            cachedRedstoneMaterials.add(Material.OAK_BUTTON);
            cachedRedstoneMaterials.add(Material.SPRUCE_BUTTON);
            cachedRedstoneMaterials.add(Material.STONE_BUTTON);
            cachedRedstoneMaterials.add(Material.REDSTONE_TORCH);
            cachedRedstoneMaterials.add(Material.REDSTONE_WALL_TORCH);
            cachedRedstoneMaterials.add(Material.REDSTONE_WIRE);
            cachedRedstoneMaterials.add(Material.DETECTOR_RAIL);
            cachedRedstoneMaterials.add(Material.POWERED_RAIL);
            cachedRedstoneMaterials.add(Material.REDSTONE_WIRE);
            cachedRedstoneMaterials.add(Material.REDSTONE_BLOCK);
            cachedRedstoneMaterials.add(Material.LEVER);
            cachedRedstoneMaterials.add(Material.STONE_BUTTON);
            cachedRedstoneMaterials.add(Material.TRIPWIRE);
            cachedRedstoneMaterials.add(Material.TRIPWIRE_HOOK);
            cachedRedstoneMaterials.add(Material.DAYLIGHT_DETECTOR);
            if (cachedBooleanValues.get("use-plates")) {
                cachedRedstoneMaterials.add(Material.BIRCH_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.ACACIA_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.DARK_OAK_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.JUNGLE_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.OAK_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.SPRUCE_PRESSURE_PLATE);
                cachedRedstoneMaterials.add(Material.STONE_PRESSURE_PLATE);
            }
        }

        List<String> configMatList = getStringList("customMaterials");

        for (String mat : configMatList) {
            cachedRedstoneMaterials.add(Material.getMaterial(mat));
        }

        getString(PATH_MATERIALS_USEITEM);
    }

    public void clearCachedValues() {
        cachedRedstoneMaterials.clear();
        cachedBooleanValues.clear();
    }

    private boolean areMaterialsConfigured() {
        return getBoolean(PATH_MATERIALS_CONFIGURED);
    }
}

