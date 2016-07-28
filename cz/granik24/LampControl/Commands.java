package cz.granik24.LampControl;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
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
            sender.sendMessage(Main.pluginPrefix + ChatColor.RED + "Too many arguments!");
            return true;
        } else if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("lampcontrol.worldedit") || p.isOp()) {
                if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                    p.sendMessage(Main.pluginPrefix + ChatColor.RED + "WorldEdit isn't installed. Install it, if you need this feature.");
                    return true;
                } else {

                    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                    Selection selection = worldEdit.getSelection(p);

                    if (selection == null) {
                        p.sendMessage(Main.pluginPrefix + ChatColor.LIGHT_PURPLE + "Make a region selection first.");
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
                        p.sendMessage(Main.pluginPrefix + ChatColor.LIGHT_PURPLE + "No lamps were affected.");
                    } else
                        p.sendMessage(Main.pluginPrefix + ChatColor.WHITE + "" + affected + ChatColor.LIGHT_PURPLE + " lamps were turned on.");
                    return true;
                }
            } else {
                p.sendMessage(Main.pluginPrefix + ChatColor.RED + "You do not have permissions to perform this command");
                return true;
            }
        } else {
            sender.sendMessage("This command cannot be run from the console.");
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("lampcontrol.worldedit") || p.isOp()) {
                    if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                        p.sendMessage(Main.pluginPrefix + ChatColor.RED + "WorldEdit isn't installed. Install it, if you need this feature.");
                        return true;
                    } else {

                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection selection = worldEdit.getSelection(p);

                        if (selection == null) {
                            p.sendMessage(Main.pluginPrefix + ChatColor.LIGHT_PURPLE + "Make a region selection first.");
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
                            p.sendMessage(Main.pluginPrefix + ChatColor.LIGHT_PURPLE + "No lamps were affected.");
                        } else
                            p.sendMessage(Main.pluginPrefix + ChatColor.WHITE + "" + affected + ChatColor.LIGHT_PURPLE + " lamps were turned off.");
                        return true;
                    }
                } else {
                    p.sendMessage(Main.pluginPrefix + ChatColor.RED + "You do not have permissions to perform this command");
                    return true;
                }
            } else {
                sender.sendMessage("This command cannot be run from the console.");
                return true;
            }
        }
        return true;
    }
}