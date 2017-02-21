package cz.ceph.lampcontrol.config;

import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private static final String PATH_MATERIALS_CONFIGURED = "materials.configured";
    private static final String PATH_MATERIALS_USEITEM = "materials.useItem";
    private static final String PATH_MATERIALS_LIST = "materials.customMaterials";

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
        setBoolean(PATH_USE_PLATE_WOODEN, true);
        setBoolean(PATH_USE_PLATE_STONE, true);
        setString(PATH_LANGUAGE, "en");
        setBoolean(PATH_MANAGE_LAMPS, true);
        setBoolean(PATH_MANAGE_RAILS, true);
        setBoolean(PATH_MANAGE_OP, true);
        setBoolean(PATH_MATERIALS_CONFIGURED, false);
        setList(PATH_MATERIALS_LIST, materialsConfigDefault);
        setInt(FILE_VERSION_PATH, 7);
    }

    public String getPluginPrefix() {
        return getString(PATH_PLUGIN_PREFIX);
    }

    public void initializeConfig() {
        getMain().lampTool = Material.getMaterial(getString(PATH_LAMPTOOL));
        cachedBooleanValues.put("use-permissions", getBoolean(PATH_USE_PERMISSIONS));
        cachedBooleanValues.put("use-items", getBoolean(PATH_USE_ITEMS));
        cachedBooleanValues.put("use-plate-wooden", getBoolean(PATH_USE_PLATE_WOODEN));
        cachedBooleanValues.put("use-plate-stone", getBoolean(PATH_USE_PLATE_STONE));
        cachedBooleanValues.put("manage-lamps", getBoolean(PATH_MANAGE_LAMPS));
        cachedBooleanValues.put("manage-rails", getBoolean(PATH_MANAGE_RAILS));
        cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));
        cachedBooleanValues.put("manage-op", getBoolean(PATH_MANAGE_OP));
        getMain().language = getString(PATH_LANGUAGE);

        if (!areMaterialsConfigured()) {
            cachedRedstoneMaterials.add(Material.DETECTOR_RAIL);
            cachedRedstoneMaterials.add(Material.POWERED_RAIL);
            cachedRedstoneMaterials.add(Material.REDSTONE_WIRE);
            cachedRedstoneMaterials.add(Material.REDSTONE_BLOCK);
            cachedRedstoneMaterials.add(Material.PISTON_MOVING_PIECE);
            cachedRedstoneMaterials.add(Material.REDSTONE_TORCH_OFF);
            cachedRedstoneMaterials.add(Material.REDSTONE_TORCH_ON);
            cachedRedstoneMaterials.add(Material.DIODE_BLOCK_OFF);
            cachedRedstoneMaterials.add(Material.DIODE_BLOCK_ON);
            cachedRedstoneMaterials.add(Material.REDSTONE_COMPARATOR_OFF);
            cachedRedstoneMaterials.add(Material.REDSTONE_COMPARATOR_ON);
            cachedRedstoneMaterials.add(Material.DIODE_BLOCK_ON);
            cachedRedstoneMaterials.add(Material.LEVER);
            cachedRedstoneMaterials.add(Material.STONE_BUTTON);
            cachedRedstoneMaterials.add(Material.WOOD_BUTTON);
            cachedRedstoneMaterials.add(Material.GOLD_PLATE);
            cachedRedstoneMaterials.add(Material.IRON_PLATE);
            cachedRedstoneMaterials.add(Material.TRIPWIRE);
            cachedRedstoneMaterials.add(Material.TRIPWIRE_HOOK);
            cachedRedstoneMaterials.addAll(Arrays.stream(Material.values()).filter(mat -> mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR") || mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR_INVERTED")).collect(Collectors.toList()));
            if (cachedBooleanValues.get("use-plate-wooden"))
                cachedRedstoneMaterials.add(Material.WOOD_PLATE);
            if (cachedBooleanValues.get("use-plate-stone"))
                cachedRedstoneMaterials.add(Material.STONE_PLATE);
            if (cachedBooleanValues.get("use-items")) {
                setString(PATH_MATERIALS_USEITEM, "FLINT_AND_STEEL");
            }
        } else {
            List<String> configMatList = getStringList("customMaterials");

            for (String mat : configMatList) {
                cachedRedstoneMaterials.add(Material.getMaterial(mat));
            }

            getString(PATH_MATERIALS_USEITEM);
        }
    }

    private boolean areMaterialsConfigured() {
        return getBoolean(PATH_MATERIALS_CONFIGURED);
    }
}

