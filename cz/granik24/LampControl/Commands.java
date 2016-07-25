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
        boolean off = false;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("version")) {
                sender.sendMessage(ChatColor.YELLOW + "-- " + Main.pluginInfo.getName() + " v" + Main.pluginInfo.getVersion() + " --");
                sender.sendMessage(ChatColor.RED + "/lamp or //lamp - Will turn on selected lamps");
                return true;
            }
        } else if (args.length > 2) {
            sender.sendMessage(ChatColor.RED + "Too many arguments!");
            sender.sendMessage(ChatColor.RED + "Use only /lamp or //lamp.");
            return true;
        }

        if (sender instanceof Player) {
            if (sender.hasPermission("lampcontrol.worldedit") || sender.isOp()) {
                if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                    sender.sendMessage(ChatColor.RED + Main.prefix + "WorldEdit isn't installed. Install it, if you need this feature.");
                    return true;
                } else {

                    Player player = (Player) sender;
                    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                    Selection selection = worldEdit.getSelection(player);

                    if (selection == null) {
                        sender.sendMessage(ChatColor.RED + "Make a region selection first.");
                        return true;
                    }

                    boolean checkForSelection = false;
                    if (!(selection instanceof CuboidSelection)) {
                        checkForSelection = true;
                    }

                    org.bukkit.Location min = selection.getMinimumPoint();
                    org.bukkit.Location max = selection.getMaximumPoint();
                    int affected = 0;
                    int affected2 = 0;
                    int found = 0;
                    for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                        for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
                            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)

                                if (!checkForSelection || selection.contains(new org.bukkit.Location(min.getWorld(), x, y, z))) {
                                    Block block = min.getWorld().getBlockAt(new org.bukkit.Location(min.getWorld(), x, y, z));

                                    if (block.getType().equals(Material.REDSTONE_LAMP_OFF)) {
                                        found++;
                                        try {
                                            SwitchBlock.switchLamp(block, true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        affected++;

                                    } else if (block.getType().equals(Material.REDSTONE_LAMP_ON)) {
                                        found++;
                                        try {
                                            SwitchBlock.switchLamp(block, false);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        affected2++;
                                        off = true;
                                    }
                                }
                    }

                    if (affected < 1) {
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "No lamps were affected.");
                    } else if (!off)
                        sender.sendMessage(ChatColor.WHITE + "" + affected2 + ChatColor.LIGHT_PURPLE + " out of " + ChatColor.WHITE + found + ChatColor.LIGHT_PURPLE + " lamps have been turned on.");

                    else
                        sender.sendMessage(ChatColor.WHITE + "" + affected + ChatColor.LIGHT_PURPLE + " out of " + ChatColor.WHITE + found + ChatColor.LIGHT_PURPLE + " lamps have been turned off.");

                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permissions to perform this command");
                return true;
            }
        } else {
            sender.sendMessage("This command cannot be run from the console.");
            return true;
        }
    }
}