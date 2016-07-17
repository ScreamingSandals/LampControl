package com.jacklinkproductions.LampControl;

import net.minecraft.server.v1_10_R1.World;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class SwitchBlock extends JavaPlugin
{
	public static void switchLamp(Block b, boolean lightning) throws Exception
	{
		World w = ((CraftWorld)b.getWorld()).getHandle();

		if (lightning)
		{
			setWorldStatic(w, true);
			b.setType(Material.REDSTONE_LAMP_ON);
			setWorldStatic(w, false);
		}
		else
		{
			b.setType(Material.REDSTONE_LAMP_OFF);
		}
	}
	
	private static void setWorldStatic(World world, boolean static_boolean) throws Exception {
		java.lang.reflect.Field static_field = World.class.getDeclaredField("isClientSide");
		
		static_field.setAccessible(true);
		static_field.set(world, static_boolean);
	}
}