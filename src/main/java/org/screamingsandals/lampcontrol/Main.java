package org.screamingsandals.lampcontrol;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.screamingsandals.lampcontrol.api.SwitchBlock;
import org.screamingsandals.lampcontrol.config.BaseConfig;
import org.screamingsandals.lampcontrol.config.MainConfig;
import org.screamingsandals.lampcontrol.environment.Environment;
import org.screamingsandals.lampcontrol.environment.LegacyEnvironment;
import org.screamingsandals.lampcontrol.environment.MainEnvironment;
import org.screamingsandals.lampcontrol.listeners.PlayerListener;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.lang.Language;
import org.screamingsandals.lib.screamingcommands.ScreamingCommands;

import java.io.File;
import java.io.IOException;

import static org.screamingsandals.lib.lang.I.mpr;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    @Getter
    private static BaseConfig baseConfig;
    @Getter
    private static Environment environment;
    private ScreamingCommands screamingCommands;
    @Getter
    private final static SwitchBlock switchBlock = new org.screamingsandals.lampcontrol.world.SwitchBlock();
    @Getter
    private static WorldEditPlugin worldEdit;
    private boolean worldEditInstalled = false;
    private Economy economy;
    private boolean isVault = false;

    @Override
    public void onEnable() {
        instance = this;

        PaperLib.suggestPaper(this);
        if (PaperLib.isVersion(13)) {
            environment = new MainEnvironment(this);
        } else {
            environment = new LegacyEnvironment(this);
        }

        System.out.println(environment.toString());

        baseConfig = new MainConfig(createConfigFile(getDataFolder(), "config.yml"));
        baseConfig.initialize();
        baseConfig.checkDefaultValues();

        screamingCommands = new ScreamingCommands(this, Main.class, "cz/ceph/lampcontrol");
        screamingCommands.loadCommands();

        Language language = new Language(this, baseConfig.getString("language"));

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        try {
            worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            if (worldEdit != null) {
                worldEditInstalled = true;
            }
        } catch (NoClassDefFoundError ignored) {
        }

        if (baseConfig.getBoolean("vault.enabled")) {
            log(setupEconomy() ? mpr("vault.loaded").get() : mpr("vault.not_found").get());
        }

        log(mpr("loaded")
                .replace("%boolean%", worldEditInstalled)
                .get());
    }

    @Override
    public void onDisable() {
        screamingCommands.unloadCommands();

        getServer().getServicesManager().unregisterAll(this);
    }

    public static boolean isWorldEdit() {
        return instance.worldEditInstalled;
    }

    public static boolean isLegacy() {
        return environment instanceof LegacyEnvironment;
    }

    private File createConfigFile(File dataFolder, String fileName) {
        dataFolder.mkdirs();

        File configFile = new File(dataFolder, fileName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return configFile;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> registration = getServer().getServicesManager().getRegistration(Economy.class);
        if (registration == null) {
            return false;
        }

        economy = registration.getProvider();
        isVault = true;
        return true;
    }

    public static void withdrawPlayer(Player player) {
        double count = getBaseConfig().getDouble("vault.cost");
        try {
            if (instance.isVault && getBaseConfig().getBoolean("vault.enable")) {
                EconomyResponse response = instance.economy.withdrawPlayer(player, count);
                if (response.transactionSuccess()) {
                    mpr("vault.withdraw")
                            .replace("%count%", Double.toString(count))
                            .replace("%currency%", (count == 1
                                    ? instance.economy.currencyNameSingular()
                                    : instance.economy.currencyNamePlural())).send(player);
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static void log(String message) {
        instance.getLogger().info(message);
    }
}
