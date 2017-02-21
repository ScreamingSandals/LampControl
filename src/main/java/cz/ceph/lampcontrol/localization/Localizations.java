package cz.ceph.lampcontrol.localization;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Localizations {

    private LampControl plugin;

    private HashMap<String, YamlConfiguration> langFiles;

    public Localizations(LampControl plugin) {
        this.plugin = plugin;
        langFiles = new HashMap<>();
    }

    public void loadLocales() {
        try {
            File langFile = new File(plugin.getDataFolder(), "languages");

            if (langFile.exists() && langFile.isDirectory()) {
                for (File file : langFile.listFiles()) {
                    if (file.getName().startsWith("lang_")) {
                        String[] parts = file.getName().split("_");
                        langFiles.put(parts[1].replace(".yml", ""), YamlConfiguration.loadConfiguration(file));
                    }
                }
            } else {
                LampControl.debug.warning("No languages found!");
            }
        } catch (NullPointerException e) {
            LampControl.debug.warning("There was an error during loading of languages!");
        }
    }

    public Set<String> getAvailableLanguages() {
        return langFiles.keySet();
    }

    public String get(String key) {
        String lang = LampControl.language;
        return get(lang, key, "{d" + lang + ":" + key + "}");
    }

    public String get(String lang, String key, String fallback) {
        YamlConfiguration language = langFiles.get(lang);
        if (language == null) return fallback;

        return language.getString(key, fallback);
    }
}
