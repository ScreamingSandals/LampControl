/*
	Code has been adapted from richie3366's LumosMaxima.
	GNU General Public License version 3 (GPLv3)
*/

package com.jacklinkproductions.LampControl;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
		@SuppressWarnings("deprecation")
		int typeID = e.getBlock().getTypeId();
		
		if (((typeID == 123) || (typeID == 124) || (typeID == 27)) && (!this.plugin.isRedstoneMaterial(e.getChangedType())))
		{
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) throws Exception
	{
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if (e.getPlayer().isSneaking()) return;
		
		if ((Main.opUsesHand == "true" && e.getPlayer().isOp()) || (e.getPlayer().hasPermission("lampcontrol.hand")))
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
			if (Main.usePermissions == "true" && !e.getPlayer().hasPermission("lampcontrol.use")) return;
			
			if (Main.toggleLamps == "false") return;
			
			e.setCancelled(true);
			
			Block b = e.getClickedBlock();
			BlockState blockState = b.getState();
			
			SwitchBlock.switchLamp(b, false);
			
			BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_OFF), e.getPlayer(), true);
			Bukkit.getPluginManager().callEvent(checkBuildPerms);
			
			if (checkBuildPerms.isCancelled())
			{
				SwitchBlock.switchLamp(b, true);
				e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here !");
				return;
			}
			
			if (Main.takeItemOnUse == "true")
			{
				ItemStack item = e.getPlayer().getItemInHand();
		        item.setAmount(item.getAmount() - 1);
		        e.getPlayer().setItemInHand(null);
			}
			
			e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.CLICK, 0.5F, 0.0F);
			
			return;
		}

		if (e.getClickedBlock().getType().equals(Material.POWERED_RAIL))
		{
			if (Main.usePermissions == "true" && !e.getPlayer().hasPermission("lampcontrol.use")) return;
		
			if (Main.controlRails == "false") return;
			
			e.setCancelled(true);
			
			Block b = e.getClickedBlock();
			BlockState blockState = b.getState();
			int data = (int) b.getData();
			
			if (data < 7)
			{
				BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
				Bukkit.getPluginManager().callEvent(checkBuildPerms);
				
				if (checkBuildPerms.isCancelled())
				{
					e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here !");
					return;
				}
				else
				{				
					int i = 1;
					while (i < 9)
					{
						Block rb = b.getRelative(BlockFace.NORTH, i);
						int rdata = (int) rb.getData();
						
						if ((rb.getType().equals(Material.POWERED_RAIL)) && (rdata < 7))
						{
							SwitchBlock.switchRail(rb, true);
						}
						else
						{
							break;
						}
						
						i++;
					}
					i = 1;
					while (i < 9)
					{
						Block rb = b.getRelative(BlockFace.SOUTH, i);
						int rdata = (int) rb.getData();
						
						if ((rb.getType().equals(Material.POWERED_RAIL)) && (rdata < 7))
						{
							SwitchBlock.switchRail(rb, true);
						}
						else
						{
							break;
						}
						
						i++;
					}
					i = 1;
					while (i < 9)
					{
						Block rb = b.getRelative(BlockFace.EAST, i);
						int rdata = (int) rb.getData();
						
						if ((rb.getType().equals(Material.POWERED_RAIL)) && (rdata < 7))
						{
							SwitchBlock.switchRail(rb, true);
						}
						else
						{
							break;
						}
						
						i++;
					}
					i = 1;
					while (i < 9)
					{
						Block rb = b.getRelative(BlockFace.WEST, i);
						int rdata = (int) rb.getData();
						
						if ((rb.getType().equals(Material.POWERED_RAIL)) && (rdata < 7))
						{
							SwitchBlock.switchRail(rb, true);
						}
						else
						{
							break;
						}
						
						i++;
					}

					SwitchBlock.switchRail(b, true);
				}
				
				if (Main.takeItemOnUse == "true")
				{
					ItemStack item = e.getPlayer().getItemInHand();
			        item.setAmount(item.getAmount() - 1);
			        e.getPlayer().setItemInHand(null);
			        e.getPlayer().updateInventory();
				}
				
				e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.CLICK, 0.5F, 1.0F);
				
				return;
			}
			else
			{				
				BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
				Bukkit.getPluginManager().callEvent(checkBuildPerms);
				
				if (checkBuildPerms.isCancelled())
				{
					e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here !");
					return;
				}
				else
				{
					SwitchBlock.switchRail(b, false);
				}
				
				if (Main.takeItemOnUse == "true")
				{
					ItemStack item = e.getPlayer().getItemInHand();
			        item.setAmount(item.getAmount() - 1);
			        e.getPlayer().setItemInHand(null);
				}
				
				e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.CLICK, 0.5F, 0.0F);
				
				return;
			}
		}
		
		if (!e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) return;
		
		e.setCancelled(true);
		
		if (Main.usePermissions == "true" && !e.getPlayer().hasPermission("lampcontrol.use")) return;
		
		Block b = e.getClickedBlock();
		BlockState blockState = b.getState();
		
		SwitchBlock.switchLamp(b, true);
		
		BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
		Bukkit.getPluginManager().callEvent(checkBuildPerms);
		
		if (checkBuildPerms.isCancelled())
		{
			SwitchBlock.switchLamp(b, false);
			e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here!");
			return;
		}
		
		if (Main.takeItemOnUse == "true")
		{
			ItemStack item = e.getPlayer().getItemInHand();
	        item.setAmount(item.getAmount() - 1);
	        e.getPlayer().setItemInHand(null);
		}

		e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.CLICK, 0.5F, 1.0F);
	}
}