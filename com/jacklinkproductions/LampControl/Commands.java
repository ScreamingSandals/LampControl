/*
	Thank you to rsod for providing WorldEdit code.
	GNU General Public License version 3 (GPLv3)
*/

package com.jacklinkproductions.LampControl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
		  sender.sendMessage("This command cannot be run from the console.");
		  return true;
		}
		
		Player p = (Player)sender;
		
		if (!p.hasPermission("lampcontrol.worldedit")) 
		{
			p.sendMessage(ChatColor.RED + "You do not have permissions to perform this command");
			return true;
		}
		
		if (args.length > 1)
		{
			p.sendMessage(ChatColor.RED + "Too many arguments:");
			p.sendMessage(ChatColor.RED + "/lamp [percentage]");
			return true;
		}
		
		int percent = 100;
		if (args.length == 1) {
			try {
				percent = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				p.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a percentage.");
				p.sendMessage(ChatColor.RED + "/lamp [percentage]");
				return true;
			}
			if ((percent <= 0) || (percent >= 100)) {
				p.sendMessage(ChatColor.RED + "Percentage must be between 0 and 100!");
				return true;
			}
		}
	
		if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
			p.sendMessage(ChatColor.RED + Main.prefix + "WorldEdit not installed.");
			return true;
		}
		
		WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		Selection selection = worldEdit.getSelection(p);
		
		if (selection == null) {
			p.sendMessage(ChatColor.RED + "Make a region selection first.");
			return true;
		}
		
		boolean checkForSelection = false;
		if (!(selection instanceof CuboidSelection)){
			checkForSelection = true;
		}
		
		org.bukkit.Location min = selection.getMinimumPoint();
		org.bukkit.Location max = selection.getMaximumPoint();
		int affected = 0;
		int found = 0;
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
		{
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
			for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
			if ((!checkForSelection) || (selection.contains(new org.bukkit.Location(min.getWorld(), x, y, z))))
			{
				Block block = min.getWorld().getBlockAt(new org.bukkit.Location(min.getWorld(), x, y, z));
				if (block.getType().equals(Material.REDSTONE_LAMP_OFF))
				{
					found++;
					if ((percent == 100) || ((int)(Math.random() * 100.0D) < percent))
					{
						Main.switchLamp(block, true);
						affected++;
					}
				}
			}
		}
		p.sendMessage(ChatColor.LIGHT_PURPLE + "" + affected + " out of " + found + " lamp(s) have been turned on.");
		return true;
	}
}