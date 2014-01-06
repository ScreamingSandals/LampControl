package com.jacklinkproductions.LampControl;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.server.v1_7_R1.WorldServer;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	public static String prefix = "&6[LampControl] ";
	private ArrayList<Material> redstone_materials = new ArrayList<Material>();
	public Material toolMaterial;
	public static String usePermissions = "false";
	public String woodPlateControl = "false";
	public String stonePlateControl = "false";
	public static String opUsesHand = "true";
	public static String takeItemOnUse = "false";
	
    private static PluginDescriptionFile pdfFile;
    
    @Override
    public void onDisable() {
        // Output info to console on disable
        getLogger().info("Thanks for using LampControl!");
    }
	
	public void onEnable() {
    	
        // Create default config if not exist yet.
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        // Load configuration.
        reloadConfiguration();

        // Register command executor.
        Commands commandExecutor = new Commands();
        getCommand("lampcontrol").setExecutor(commandExecutor);
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("lc").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        // Output info to console on load
        pdfFile = this.getDescription();
        getLogger().info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		
		this.redstone_materials.add(Material.DETECTOR_RAIL);
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

        // Register our events
        getServer().getPluginManager().registerEvents(new LampListener(this), this);
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
    }
	
	public boolean isRedstoneMaterial(Material mat)
	{
		if (this.redstone_materials.contains(mat)) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void switchLamp(Block b, boolean lighting)
	{
		WorldServer ws = ((CraftWorld)b.getWorld()).getHandle();
	
		boolean mem = ws.isStatic;
		if (!mem) ws.isStatic = true;
	
		if (lighting)
			b.setTypeIdAndData(Material.REDSTONE_LAMP_ON.getId(), (byte)0, false);
		else {
			b.setTypeIdAndData(Material.REDSTONE_LAMP_OFF.getId(), (byte)0, false);
		}
	
		if (!mem) ws.isStatic = false;
	}
}