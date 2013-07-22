package com.jacklinkproductions.LampControl;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	public String prefix = "[LampControl] ";
	private ArrayList<Material> redstone_materials = new ArrayList<Material>();
	public Material toolMaterial;
	public boolean allowOnlyPermitted = false;
	public boolean displayLightOnMessage = true;
	public boolean displayNoPermissionsMessage = true;
	public boolean woodenPressurePlateCanInteract = false;
	public boolean stonePressurePlateCanInteract = false;
	static Logger logger;
	
	public void onEnable()
	{
		logger = this.getLogger();
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
		this.redstone_materials.add(Material.GOLD_PLATE);
		this.redstone_materials.add(Material.IRON_PLATE);
	
		if (!getConfig().isSet("activatorItemID")) {
			getConfig().set("activatorItemID", Integer.valueOf(Material.FLINT_AND_STEEL.getId()));
		}
		
		if (!getConfig().isSet("allowOnlyPermitted")) {
			getConfig().set("allowOnlyOps", "Deprecated, use allowOnlyPermitted instead. (perm to add : \"lumosmaxima.poweron\")");
			logger.info("TESTLOG");
			getConfig().set("allowOnlyPermitted", Boolean.valueOf(false));
		}
		
		if (!getConfig().isSet("displayNoPermissionsMessage")) {
			getConfig().set("displayNoPermissionsMessage", Boolean.valueOf(true));
		}
		
		if (!getConfig().isSet("woodenPressurePlateCanInteract")) {
			getConfig().set("woodenPressurePlateCanInteract", Boolean.valueOf(false));
		}
		
		if (!getConfig().isSet("stonePressurePlateCanInteract")) {
			getConfig().set("stonePressurePlateCanInteract", Boolean.valueOf(false));
		}
		
		if (!getConfig().isSet("opOnWithHand")) {
			getConfig().set("opOnWithHand", Boolean.valueOf(false));
		}
		
		saveConfig();
		
		this.toolMaterial = Material.getMaterial(getConfig().getInt("activatorItemID"));
		this.allowOnlyPermitted = getConfig().getBoolean("allowOnlyPermitted");
		this.displayLightOnMessage = getConfig().getBoolean("displayLightOnMessage");
		this.displayNoPermissionsMessage = getConfig().getBoolean("displayNoPermissionsMessage");
		this.woodenPressurePlateCanInteract = getConfig().getBoolean("woodenPressurePlateCanInteract");
		this.stonePressurePlateCanInteract = getConfig().getBoolean("stonePressurePlateCanInteract");
		LampListener.opOnWithHand = getConfig().getBoolean("opOnWithHand");
		
		if (this.toolMaterial == null) this.toolMaterial = Material.FLINT_AND_STEEL;
		
		if (this.woodenPressurePlateCanInteract) {
			this.redstone_materials.add(Material.WOOD_PLATE);
		}
		
		if (this.stonePressurePlateCanInteract) {
			this.redstone_materials.add(Material.STONE_PLATE);
		}
		
		getServer().getPluginManager().registerEvents(new LampListener(this), this);
	}
	
	public boolean isRedstoneMaterial(Material mat)
	{
		if (this.redstone_materials.contains(mat)) {
			return true;
		}
		return false;
	}
}