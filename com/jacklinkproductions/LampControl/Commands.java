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

import com.jacklinkproductions.LampControl.Updater;
import com.jacklinkproductions.LampControl.Updater.UpdateResult;

public class Commands implements CommandExecutor
{
    private final Main plugin;

    Commands(Main plugin) {
        this.plugin = plugin;
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		int percent = 100;
		boolean off = false;
			
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("version"))
			{
				sender.sendMessage(ChatColor.YELLOW + "-- " + Main.pdfFile.getName() + " v" + Main.pdfFile.getVersion() + " --");
				sender.sendMessage(ChatColor.RED + "/lamp [off] [percentage] - For a WorldEdit Selection");
				sender.sendMessage(ChatColor.RED + "/lamp update - Updates to latest version");
				return true;
			}
			else if (args[0].equalsIgnoreCase("update"))
			{
				if (sender.hasPermission("lampcontrol.update") || sender.isOp()) 
				{
			        if (plugin.getConfig().getString("update-notification") == "false")
			        {
			            sender.sendMessage(ChatColor.RED + "This command is disabled in the config!");
			            return true;
			        }
			        
		            if(!plugin.updateAvailable) {
		                sender.sendMessage(ChatColor.YELLOW + "No updates are available!");
		                return true;
		            }
		            
		            Updater updater = new Updater(plugin, Main.updaterID, plugin.getFile(), Updater.UpdateType.DEFAULT, true);
		            if(updater.getResult() == UpdateResult.NO_UPDATE)
		                sender.sendMessage(ChatColor.YELLOW + "No updates are available!");
		            else
		            {
		                sender.sendMessage(ChatColor.YELLOW + "Updating... Check console for details.");
		            }
		            
		            return true;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "You do not have permissions to perform this command");
					return true;
				}
			}
			else if (args[0].equalsIgnoreCase("off"))
			{
				percent = 100;
				off = true;
			}
			else
			{
				try {
					percent = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a percentage.");
					sender.sendMessage(ChatColor.RED + "/lamp [off] [percentage]");
					return true;
				}
				if ((percent <= 0) || (percent >= 100)) {
					sender.sendMessage(ChatColor.RED + "Percentage must be between 0 and 100!");
					return true;
				}
			}
		}
		else if (args.length > 1)
		{
			if (args.length == 2 && args[0].equalsIgnoreCase("off"))
			{
				off = true;
				
				try {
					percent = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a percentage.");
					sender.sendMessage(ChatColor.RED + "/lamp [off] [percentage]");
					return true;
				}
				if ((percent <= 0) || (percent >= 100)) {
					sender.sendMessage(ChatColor.RED + "Percentage must be between 0 and 100!");
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Too many arguments:");
				sender.sendMessage(ChatColor.RED + "/lamp [off] [percentage]");
				return true;
			}
		}
		else
		{
			percent = 100;
		}

		if (sender instanceof Player)
		{
			if (sender.hasPermission("lampcontrol.worldedit") || sender.isOp()) 
			{
				if (Main.versionOk == "true")
				{
					if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null)
					{
						sender.sendMessage(ChatColor.RED + Main.prefix + "WorldEdit not installed.");
						return true;
					}
					else
					{
						
						Player player = (Player) sender;
						WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
						Selection selection = worldEdit.getSelection(player);
						
						if (selection == null) {
							sender.sendMessage(ChatColor.RED + "Make a region selection first.");
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
										try {
											SwitchBlock.switchLamp(block, true);
										} catch (Exception e) {
											e.printStackTrace();
										}
										affected++;
									}
								}
								else if (block.getType().equals(Material.REDSTONE_LAMP_ON) && off == true)
								{
									found++;
									if ((percent == 100) || ((int)(Math.random() * 100.0D) < percent))
									{
										try {
											SwitchBlock.switchLamp(block, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
										affected++;
									}
								}
							}
						}
						
						if (off == false)
							sender.sendMessage(ChatColor.LIGHT_PURPLE + "" + affected + " out of " + found + " lamp(s) have been turned on.");
						else
							sender.sendMessage(ChatColor.LIGHT_PURPLE + "" + affected + " out of " + found + " lamp(s) have been turned off.");
						
						return true;
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "This version of Craftbukkit is not compatible! Try /lc update");
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have permissions to perform this command");
				return true;
			}
		}
		else
		{
			sender.sendMessage("This command cannot be run from the console.");
			return true;
		}
	}
}