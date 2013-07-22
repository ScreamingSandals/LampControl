/*
	Code has been adapted from richie3366's LumosMaxima.
	GNU General Public License version 3 (GPLv3)
*/

package com.jacklinkproductions.LampControl;


import net.minecraft.server.v1_6_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class LampListener implements Listener
{
	public static boolean opOnWithHand = false;
	Main plugin = null;
	
	public LampListener(Main p) {
		this.plugin = p;
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e)
	{
		int typeID = e.getBlock().getTypeId();
		
		if (((typeID == 123) || (typeID == 124)) && (!this.plugin.isRedstoneMaterial(e.getChangedType())))
		{
			e.setCancelled(true);
			//LMPlugin.logger.info(typeID+" TO "+e.getChangedType());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if (e.getPlayer().isOp() && opOnWithHand)
		{
			if ((e.getPlayer().getItemInHand() == null) || (!e.getPlayer().getItemInHand().getType().equals(Material.AIR) && !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)))
			{
				return;
			}
		}
		else
		{
			if ((e.getPlayer().getItemInHand() == null) || (!e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)))
			{
				return;
			}
		}
	
		if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_ON))
		{
			if (this.plugin.displayLightOnMessage)
			{
				e.getPlayer().sendMessage(ChatColor.RED + this.plugin.prefix + "This lamp is already on !");
			}
			return;
		}
		
		if (!e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) return;
		
		e.setCancelled(true);
		
		if ((this.plugin.allowOnlyPermitted) && (!e.getPlayer().hasPermission("lampcontrol.use"))) return;
		
		Block b = e.getClickedBlock();
		BlockState blockState = b.getState();
		
		switchLamp(b, true);
		
		BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
		Bukkit.getPluginManager().callEvent(checkBuildPerms);
		
		if (checkBuildPerms.isCancelled())
		{
			switchLamp(b, false);
			if (this.plugin.displayNoPermissionsMessage)
				e.getPlayer().sendMessage(ChatColor.RED + this.plugin.prefix + "You don't have permissions to build here !");
			return;
		}
	
		if (this.plugin.displayLightOnMessage)
			e.getPlayer().sendMessage(ChatColor.GREEN + this.plugin.prefix + "\"Lumos maxima !\"");
	}
	
	private void switchLamp(Block b, boolean lighting)
	{
		WorldServer ws = ((CraftWorld)b.getWorld()).getHandle();
		
		boolean mem = ws.isStatic;
		if (!mem) ws.isStatic = true;
		
		if (lighting)
		{
			b.setTypeIdAndData(Material.REDSTONE_LAMP_ON.getId(), (byte)0, false);
		}
		else
		{
			b.setTypeIdAndData(Material.REDSTONE_LAMP_OFF.getId(), (byte)0, false);
		}
		
		if (!mem) ws.isStatic = false;
	}
}