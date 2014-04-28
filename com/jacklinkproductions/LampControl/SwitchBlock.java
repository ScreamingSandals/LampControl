package com.jacklinkproductions.LampControl;

import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

public class SwitchBlock extends JavaPlugin
{
	@SuppressWarnings("deprecation")
	public static void switchLamp(Block b, boolean lighting)
	{
		WorldServer ws = ((CraftWorld)b.getWorld()).getHandle();
	
		boolean mem = ws.isStatic;
	
		if (lighting == true)
		{
			if (!mem) ws.isStatic = true;
			b.setTypeIdAndData(Material.REDSTONE_LAMP_ON.getId(), (byte)0, false);
			if (!mem) ws.isStatic = false;
		}
		else
		{
			b.setTypeIdAndData(Material.REDSTONE_LAMP_OFF.getId(), (byte)0, false);
		}
	}

	@SuppressWarnings("deprecation")
	public static void switchRail(Block b, boolean power)
	{
		WorldServer ws = ((CraftWorld)b.getWorld()).getHandle();
	
		boolean mem = ws.isStatic;
		int data = (int) b.getData();
	
		if (power == true)
		{
			data = data + 8;
			
			if (!mem) ws.isStatic = true;
			b.setTypeIdAndData(27, (byte)data, false);
			if (!mem) ws.isStatic = false;
		}
		else
		{
			data = data - 8;
			b.setTypeIdAndData(27, (byte)data, false);
		}
	}
}