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
    private static final String PATH_LANGUAGE = "language";
    private static final String PATH_LIGHT_ITEM = "lightItem";
    private static final String PATH_ENABLE_PERMISSIONS = "enable.permissions";
    private static final String PATH_ENABLE_ITEMS = "enable.items";
    private static final String PATH_ENABLE_PLATES = "enable.plates";
    private static final String PATH_CONTROL_LAMPS = "control.lamps";
    private static final String PATH_CONTROL_RAILS = "control.rails";
    private static final String PATH_CONTROL_REDSTONE = "control.redstone";
    private static final String PATH_OPERATOR_USE = "operatorUse";
    private static final String PATH_MATERIALS_CONFIGURED = "materials.configured";
    private static final String PATH_MATERIALS_USEITEM = "materials.useItem";
    private static final String PATH_SOUND_SUCCESS = "sound.success";
    private static final String PATH_SOUND_FAIL = "sound.fail";
    private static final String PATH_SOUND_DEFAULT = "sound.default";
    private boolean isConfigInitialized = false;

    private Map<String, Boolean> cachedBooleanValues = getMain().cachedBooleanValues;

    public MainConfig(File file) {
        super(file, CONFIG_VERSION);
    }

    @Override
    public void setDefault() {
        setString(PATH_PLUGIN_PREFIX, "&8[&eLamp&cControl&8]&r");
        setString(PATH_LANGUAGE, "en");
        setString(PATH_LIGHT_ITEM, "FLINT_AND_STEEL");
        setBoolean(PATH_OPERATOR_USE, true);
        setBoolean(PATH_ENABLE_PERMISSIONS, true);
        setBoolean(PATH_ENABLE_ITEMS, false);
        setBoolean(PATH_ENABLE_PLATES, true);
        setBoolean(PATH_CONTROL_LAMPS, true);
        setBoolean(PATH_CONTROL_RAILS, true);
        setBoolean(PATH_CONTROL_REDSTONE, true);
        setBoolean(PATH_MATERIALS_CONFIGURED, false);
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

        getMain().lampTool = Material.getMaterial(getString(PATH_LIGHT_ITEM));
        cachedBooleanValues.put("enable-permissions", getBoolean(PATH_ENABLE_PERMISSIONS));
        cachedBooleanValues.put("enable-items", getBoolean(PATH_ENABLE_ITEMS));
        cachedBooleanValues.put("enable-plates", getBoolean(PATH_ENABLE_PLATES));
        cachedBooleanValues.put("control-lamps", getBoolean(PATH_CONTROL_LAMPS));
        cachedBooleanValues.put("control-rails", getBoolean(PATH_CONTROL_RAILS));
        cachedBooleanValues.put("operatorControl", getBoolean(PATH_OPERATOR_USE));
        cachedBooleanValues.put("redstoneControl", getBoolean(PATH_CONTROL_REDSTONE));

        LampControl.configLanguage = getString(PATH_LANGUAGE);
        isConfigInitialized = true;

        if (cachedBooleanValues.get("enable-items")) {
            setString(PATH_MATERIALS_USEITEM, "FLINT_AND_STEEL");
        }
    }

    public void clearCachedValues() {
        cachedBooleanValues.clear();
    }
}

