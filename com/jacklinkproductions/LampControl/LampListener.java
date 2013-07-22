/*
	Code has been adapted from richie3366's LumosMaxima.
	GNU General Public License version 3 (GPLv3)
*/

package com.jacklinkproductions.LampControl;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class LampListener implements Listener
{
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
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if (e.getPlayer().isOp() && Main.opOnWithHand == "true")
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
			return;
		}
		
		if (!e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) return;
		
		e.setCancelled(true);
		
		if ((Main.allowOnlyPermitted == "true") && (!e.getPlayer().hasPermission("lampcontrol.use"))) return;
		
		Block b = e.getClickedBlock();
		BlockState blockState = b.getState();
		
		Main.switchLamp(b, true);
		
		BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
		Bukkit.getPluginManager().callEvent(checkBuildPerms);
		
		if (checkBuildPerms.isCancelled())
		{
			Main.switchLamp(b, false);
			e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here !");
			return;
		}
	}
}