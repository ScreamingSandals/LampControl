package cz.ceph.lampcontrol.localization;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Localization {

    private LampControl plugin;
    private HashMap<String, YamlConfiguration> langFiles;
    public static String resultLanguage;

    public Localization(LampControl plugin) {
        this.plugin = plugin;
        langFiles = new HashMap<>();
    }

    public void loadLocalization() {
        String configLanguage = LampControl.configLanguage;

        try {
            File langFolder = new File(plugin.getDataFolder(), "languages");

            if (langFolder.exists() && langFolder.isDirectory()) {
                try {
                    loadLanguages(langFolder, configLanguage);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                createFile(configLanguage);
                resultLanguage = configLanguage;
            }
        } catch (
                NullPointerException e)

        {
            LampControl.debug.warning("Cannot load languages, contact developer with log.");
            e.printStackTrace();
        }

    }

    private void loadFromFolder(File langFile) {
        for (File file : langFile.listFiles()) {
            if (file.getName().startsWith("lang_")) {
                String[] parts = file.getName().split("_");
                langFiles.put(parts[1].replace(".yml", ""), YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    private void createFile(String lang) {
        try {
            plugin.saveResource("languages/lang_" + lang + ".yml", true);
        } catch (IllegalArgumentException e) {
            LampControl.debug.warning("Invalid language file! Using default one.");
            plugin.saveResource("languages/lang_en.yml", true);
            resultLanguage = "en";
        }
    }

    private void loadLanguages(File langFolder, String configLanguage) {
        for (File file : langFolder.listFiles()) {
            if (file.getName().startsWith("lang_" + configLanguage)) {
                try {
                    loadFromFolder(langFolder);
                    resultLanguage = configLanguage;
                } catch (NullPointerException e) {
                    createFile(configLanguage);
                    resultLanguage = configLanguage;
                    loadFromFolder(langFolder);
                }
            } else {
                createFile(configLanguage);
                resultLanguage = configLanguage;
                loadFromFolder(langFolder);
            }
        }
    }

    public Set<String> getAvailableLanguages() {
        return langFiles.keySet();
    }

    public String get(String key) {
        String lang = resultLanguage;
        return get(lang, key, "{" + lang + "_" + key + "}");
    }

    public String get(String lang, String key, String fallback) {
        YamlConfiguration language = langFiles.get(lang);
        if (language == null) return fallback;

        return language.getString(key, fallback);
    }

}
