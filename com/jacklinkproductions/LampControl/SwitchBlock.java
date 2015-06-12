package com.jacklinkproductions.LampControl;

import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class SwitchBlock extends JavaPlugin
{
	public static void switchLamp(Block b, boolean lighting) throws Exception
	{
		World w = ((CraftWorld)b.getWorld()).getHandle();
	
		if (lighting == true)
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

	@SuppressWarnings("deprecation")
	public static void switchRail(Block b, boolean power) throws Exception
	{
		World w = ((CraftWorld)b.getWorld()).getHandle();
	
		int data = (int) b.getData();
	
		if (power == true)
		{
			data = data + 8;

			setWorldStatic(w, true);
			b.setTypeIdAndData(27, (byte)data, false);
			setWorldStatic(w, false);
		}
		else
		{
			data = data - 8;
			b.setTypeIdAndData(27, (byte)data, false);
		}
	}
	
	private static void setWorldStatic(World world, boolean static_boolean) throws Exception {
		java.lang.reflect.Field static_field = World.class.getDeclaredField("isClientSide");
		
		static_field.setAccessible(true);
		static_field.set(world, static_boolean);
	}
}