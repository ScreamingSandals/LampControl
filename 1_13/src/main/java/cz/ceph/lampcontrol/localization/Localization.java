package cz.ceph.lampcontrol.localization;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Localization {

        private LampControl plugin;
        private HashMap<String, YamlConfiguration> langFiles;
        public String resultLanguage;

        public Localization(LampControl plugin) {
            this.plugin = plugin;
            langFiles = new HashMap<>();
        }

        public void loadLocalization() {
            resultLanguage = "";
            String configLanguage = LampControl.getMain().configLanguage;

            try {
                File langFolder = new File(plugin.getDataFolder(), "languages");

                if (langFolder.exists() && langFolder.isDirectory()) {
                    if (!checkLangFolder(langFolder)) {
                        for (File file : langFolder.listFiles()) {
                            if (file.getName().contains(configLanguage)) {
                                loadFromFolder(langFolder);
                                resultLanguage = configLanguage;
                            } else {
                                createAndLoadLangFile(configLanguage, langFolder);
                            }
                        }
                    } else {
                        createAndLoadLangFile(configLanguage, langFolder);
                    }
                } else {
                    createAndLoadLangFile(configLanguage, langFolder);
                }
            } catch (NullPointerException e) {
                LampControl.debug.info("Error loading languages, contact developer.");
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

    private boolean checkLangFolder(File folder) {
        File langFolder = folder.getParentFile();
        return langFolder.isDirectory() && langFolder.list().length == 0;
    }

    private void createLangFile(String lang) {
        try {
            plugin.saveResource("languages/lang_" + lang + ".yml", true);
            resultLanguage = lang;
        } catch (IllegalArgumentException e) {
            LampControl.debug.warning("Invalid language file! Using default language.");
            plugin.saveResource("languages/lang_en.yml", true);
            resultLanguage = "en";
        }
    }

    private void createAndLoadLangFile(String configLanguage, File langFolder) {
        createLangFile(configLanguage);
        loadFromFolder(langFolder);
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