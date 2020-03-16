package org.screamingsandals.lampcontrol.config;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ScreamingSandals team
 */
public interface BaseConfig {
    Object get(String key);

    String getString(String key);

    String getString(String key, String def);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean def);

    int getInt(String key);

    int getInt(String key, int def);

    double getDouble(String key);

    double getDouble(String key, double def);

    List<?> getList(String key);

    List<Map<?, ?>> getMap(String key);

    List<String> getStringList(String key);

    boolean isSet(String key);

    void set(String key, Object obj);

    void save();

    void initialize();

    void checkDefaultValues();

    Material getMaterial(String key);

    default void checkOrSet(AtomicBoolean modify, String path, java.io.Serializable value) {
        if (!isSet(path)) {
            set(path, value);
            modify.set(true);
        }
    }
}