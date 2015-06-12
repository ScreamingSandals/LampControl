package com.jacklinkproductions.LampControl;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.jacklinkproductions.LampControl.Updater;

public class Main extends JavaPlugin
{
	public static String prefix = "[LampControl] ";
	private ArrayList<Material> redstone_materials = new ArrayList<Material>();
	public Material toolMaterial;
	public static String usePermissions = "false";
	public String woodPlateControl = "false";
	public String stonePlateControl = "false";
	public static String opUsesHand = "true";
	public static String toggleLamps = "true";
	public static String takeItemOnUse = "false";
	public static String controlRails = "false";
	public static String versionOk = "false";
	
	public static String correctVersion = "v1_8_R3";
	public static int updaterID = 62770;
	
    static PluginDescriptionFile pdfFile;

    @Override
    public void onDisable() {
        // Output info to console on disable
        getLogger().info("Thanks for using LampControl!");
    }

    @Override
	public void onEnable() {
    	
        // Create default config if not exist yet.
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        // Load configuration.
        reloadConfiguration();
        
        // Check for old config
        if ((getConfig().isSet("config-version") == false) || (getConfig().getInt("config-version") < 4))
        {
            File file = new File(this.getDataFolder(), "config.yml");
            file.delete();
            saveDefaultConfig();
            getLogger().info( "Created a new config.yml for this version." );
        }
        
        // Setup Updater system
        if (getConfig().getString("update-notification") == "true")
        {
        	checkForUpdates();
        }
		
		// Check if Craftbukkit is compatible with this version
		final String packageName = getServer().getClass().getPackage().getName();
        String cbversion = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (cbversion.equals(correctVersion))
        {
        	versionOk = "true";
            getServer().getPluginManager().registerEvents(new LampListener(this), this);
        }
        else
        {
        	getLogger().severe("This version is for Craftbukkit " + correctVersion + " only.");
        	getLogger().severe("The plugin will load to notify you of updates, but will not work.");
        }

        // Register command executor.
        Commands commandExecutor = new Commands(this);
        getCommand("lampcontrol").setExecutor(commandExecutor);
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("lc").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        // Output info to console on load
        pdfFile = this.getDescription();
        getLogger().info( pdfFile.getName() + " v" + pdfFile.getVersion() + " is enabled!" );
		
		this.redstone_materials.add(Material.DETECTOR_RAIL);
		this.redstone_materials.add(Material.POWERED_RAIL);
		this.redstone_materials.add(Material.REDSTONE_WIRE);
		this.redstone_materials.add(Material.REDSTONE_BLOCK);
		this.redstone_materials.add(Material.PISTON_MOVING_PIECE);
		this.redstone_materials.add(Material.REDSTONE_TORCH_OFF);
		this.redstone_materials.add(Material.REDSTONE_TORCH_ON);
		this.redstone_materials.add(Material.DIODE_BLOCK_OFF);
		this.redstone_materials.add(Material.DIODE_BLOCK_ON);
		this.redstone_materials.add(Material.REDSTONE_COMPARATOR_OFF);
		this.redstone_materials.add(Material.REDSTONE_COMPARATOR_ON);
		this.redstone_materials.add(Material.DIODE_BLOCK_ON);
		this.redstone_materials.add(Material.LEVER);
		this.redstone_materials.add(Material.STONE_BUTTON);
		this.redstone_materials.add(Material.WOOD_BUTTON);
		this.redstone_materials.add(Material.GOLD_PLATE);
		this.redstone_materials.add(Material.IRON_PLATE);
		this.redstone_materials.add(Material.TRIPWIRE);
		this.redstone_materials.add(Material.TRIPWIRE_HOOK);
		if (this.stonePlateControl == "true")
			this.redstone_materials.add(Material.WOOD_PLATE);
		if (this.woodPlateControl == "true")
			this.redstone_materials.add(Material.STONE_PLATE);
	}

	@SuppressWarnings("deprecation")
	public void reloadConfiguration() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
		toolMaterial = Material.getMaterial(getConfig().getInt("lampControlItem"));
		usePermissions = getConfig().getString("usePermissions");
		woodPlateControl = getConfig().getString("woodPlateControl");
        stonePlateControl = getConfig().getString("stonePlateControl");
        opUsesHand = getConfig().getString("opUsesHand");
        takeItemOnUse = getConfig().getString("takeItemOnUse");
        toggleLamps = getConfig().getString("toggleLamps");
        controlRails = getConfig().getString("controlRails");
    }
	
	public boolean isRedstoneMaterial(Material mat)
	{
		if (this.redstone_materials.contains(mat)) {
			return true;
		}
		return false;
	}

    public boolean updateAvailable = false;
    String latestVersion = null;

    public void checkForUpdates()
    {
        final Updater updater = new Updater(this, updaterID, getFile(), Updater.UpdateType.NO_DOWNLOAD, true); // Start Updater but just do a version check
        updateAvailable = (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE); // Determine if there is an update ready for us
        latestVersion = updater.getLatestName();
        getLogger().info(latestVersion + " is the latest version available, and the updatability of it is: " + updater.getResult().name() + ". The latest version is for " + updater.getLatestGameVersion() + " only.");

        if(updateAvailable)
        {
            for (Player player : getServer().getOnlinePlayers())
            {
                if (player.hasPermission("lampcontrol.update"))
                {
                    player.sendMessage(ChatColor.YELLOW + "An update is available: " + latestVersion);
                    player.sendMessage(ChatColor.YELLOW + "This update is for " + updater.getLatestGameVersion() + " only!");
                    player.sendMessage(ChatColor.YELLOW + "Type /lc update if you would like to update.");
                }
            }

            getServer().getPluginManager().registerEvents(new Listener()
            {
                @EventHandler
                public void onPlayerJoin (PlayerJoinEvent event)
                {
                    Player player = event.getPlayer();
                    if (player.hasPermission("lampcontrol.update"))
                    {
                        player.sendMessage(ChatColor.YELLOW + "An update is available: " + latestVersion);
                        player.sendMessage(ChatColor.YELLOW + "This update is for " + updater.getLatestGameVersion() + " only!");
                        player.sendMessage(ChatColor.YELLOW + "Type /lc update if you would like to update.");
                    }
                }
            }, this);
        }
    }
    
    @Override
    public File getFile() {

        return super.getFile();
    }
}