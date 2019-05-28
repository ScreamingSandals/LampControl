package cz.ceph.lampcontrol.config;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class BaseConfiguration {

    public static final String FILE_VERSION_PATH = "version";

    protected File configFile;
    protected YamlConfiguration yaml;

    public BaseConfiguration(File file) {
        configFile = file;

        if (!file.exists()) createFile();

        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public BaseConfiguration(File file, int expectedVersion) {
        this(file);

        if (getVersion() != expectedVersion) {
            LampControl.debug.info("Creating new config file!");
            setDefault();
            setInt(FILE_VERSION_PATH, expectedVersion);
            save();
        }
    }

    public abstract void setDefault();

    public void save() {
        try {
            yaml.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createFile() {
        try {
            return configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getVersion() {
        return yaml.getInt(FILE_VERSION_PATH, 10);
    }

    protected boolean contains(String path) {
        return yaml.contains(path);
    }

    protected int getInt(String path) {
        return yaml.getInt(path);
    }

    protected int getInt(String path, int def) {
        return yaml.getInt(path, def);
    }

    protected void setInt(String path, int value) {
        yaml.set(path, value);
    }

    protected double getDouble(String path) {
        return yaml.getDouble(path);
    }

    protected double getDouble(String path, int def) {
        return yaml.getDouble(path, def);
    }

    protected void setDouble(String path, double value) {
        yaml.set(path, value);
    }

    protected String getString(String path) {
        return yaml.getString(path);
    }

    protected String getString(String path, String def) {
        return yaml.getString(path, def);
    }

    protected void setString(String path, String value) {
        yaml.set(path, value);
    }

    protected boolean getBoolean(String path) {
        return yaml.getBoolean(path);
    }

    protected boolean getBoolean(String path, boolean def) {
        return yaml.getBoolean(path, def);
    }

    protected void setBoolean(String path, boolean value) {
        yaml.set(path, value);
    }

    protected ConfigurationSection createSection(String path) {
        return yaml.createSection(path);
    }

    protected List<?> getList(String path) {
        return yaml.getList(path);
    }

    protected void setList(String path, List<?> value) {
        yaml.set(path, value);
    }

    protected List<String> getStringList(String path) {
        return yaml.getStringList(path);
    }

    protected List<Integer> getIntegerList(String path) {
        return yaml.getIntegerList(path);
    }


}
