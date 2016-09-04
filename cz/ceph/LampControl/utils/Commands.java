/*
	Code has been adapted from jacklink01.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.LampControl.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import cz.ceph.LampControl.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("off")) {
                sender.sendMessage(ChatColor.YELLOW + "-- " + Main.pluginInfo.getName() + " v" + Main.pluginInfo.getVersion() + " --");
                sender.sendMessage(ChatColor.RED + "/lamp or //lamp - Will turn on selected lamps");
                sender.sendMessage(ChatColor.RED + "/lamp off or //lamp off - Will turn off selected lamps");
                return true;
            }
        } else if (args.length >= 2) {
            sender.sendMessage(MessagesManager.PREFIX + MessagesManager.TOO_MANY_ARGUMENTS.toString());
            return true;
        } else if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("lampcontrol.worldedit") || p.isOp()) {
                if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                    p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_WORLDEDIT.toString());
                    return true;
                } else {

                    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                    Selection selection = worldEdit.getSelection(p);

                    if (selection == null) {
                        p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_SELECTION.toString());
                        return true;
                    }

                    boolean checkForSelection = false;
                    if (!(selection instanceof CuboidSelection)) {
                        checkForSelection = true;
                    }

                    org.bukkit.Location min = selection.getMinimumPoint();
                    org.bukkit.Location max = selection.getMaximumPoint();
                    int affected = 0;
                    for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                        for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
                            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)

                                if (!checkForSelection || selection.contains(new org.bukkit.Location(min.getWorld(), x, y, z))) {
                                    Block block = min.getWorld().getBlockAt(new org.bukkit.Location(min.getWorld(), x, y, z));
                                    if (block.getType().equals(Material.REDSTONE_LAMP_OFF)) {
                                        try {
                                            SwitchBlock.switchLamp(block, true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        affected++;
                                    }
                                }
                    }

                    if (affected < 1) {
                        p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_LAMPS_AFFECTED.toString());
                    } else
                        p.sendMessage(MessagesManager.PREFIX + MessagesManager.ON_LAMPS.toString().replace("%affected", "" + affected + ""));
                    return true;
                }
            } else {
                p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
                return true;
            }
        } else {
            sender.sendMessage(MessagesManager.PREFIX + "This command cannot be run from the console.");
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("lampcontrol.worldedit") || p.isOp()) {
                    if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                        p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_WORLDEDIT.toString());
                        return true;
                    } else {

                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection selection = worldEdit.getSelection(p);

                        if (selection == null) {
                            p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_SELECTION.toString());
                            return true;
                        }

                        boolean checkForSelection = false;
                        if (!(selection instanceof CuboidSelection)) {
                            checkForSelection = true;
                        }

                        org.bukkit.Location min = selection.getMinimumPoint();
                        org.bukkit.Location max = selection.getMaximumPoint();
                        int affected = 0;
                        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
                                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)

                                    if (!checkForSelection || selection.contains(new org.bukkit.Location(min.getWorld(), x, y, z))) {
                                        Block block = min.getWorld().getBlockAt(new org.bukkit.Location(min.getWorld(), x, y, z));
                                        if (block.getType().equals(Material.REDSTONE_LAMP_ON)) {
                                            try {
                                                SwitchBlock.switchLamp(block, false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            affected++;
                                        }
                                    }
                        }

                        if (affected < 1) {
                            p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_LAMPS_AFFECTED.toString());
                        } else
                            p.sendMessage(MessagesManager.PREFIX + MessagesManager.OFF_LAMPS.toString().replace("%affected", "" + affected + ""));
                        return true;
                    }
                } else {
                    p.sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
                    return true;
                }
            } else {
                sender.sendMessage(MessagesManager.PREFIX + MessagesManager.CONSOLE.toString());
                return true;
            }
        }
        return true;
    }
}