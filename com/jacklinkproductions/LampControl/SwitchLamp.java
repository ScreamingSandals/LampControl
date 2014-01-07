package com.jacklinkproductions.LampControl;

import net.minecraft.server.v1_7_R1.WorldServer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

public class SwitchLamp extends JavaPlugin
{
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