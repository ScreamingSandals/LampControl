package cz.ceph.lampcontrol.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "off")
public class OffCommand implements IBasicCommand {

    public String getPermission() {
        return "lampcontrol.command.off";
    }

    public String getDescription() {
        return "Off command that will set off all lamp in selection.";
    }

    public String getUsage() {
        return "/lampcontrol off";
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            player.sendMessage(getMain().getLocalizations().get(getMain().language, "error.no_worldedit"));
            return true;

        } else {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            Selection selection = worldEdit.getSelection(player);

            if (selection == null) {
                player.sendMessage(getMain().getLocalizations().get(getMain().language, "error.no_selection"));
                return true;
            }

            boolean checkForSelection = false;
            if (!(selection instanceof CuboidSelection)) {
                checkForSelection = true;
            }

            org.bukkit.Location min = selection.getMinimumPoint();
            org.bukkit.Location max = selection.getMaximumPoint();
            getMain().getSwitchBlock().initWorld(min.getWorld());

            int affected = 0;
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                        Location loc = new Location(min.getWorld(), x, y, z);

                        if (!checkForSelection || selection.contains(loc)) {
                            Block block = min.getWorld().getBlockAt(loc);

                            if (block.getType().equals(Material.REDSTONE_LAMP_ON)) {
                                try {
                                    getMain().getSwitchBlock().switchLamp(block, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                affected++;
                            }
                        }
                    }
            }

            if (affected < 1) {
                player.sendMessage(getMain().getLocalizations().get(getMain().language, "info.no_lamps_affecetd"));
            } else
                player.sendMessage(getMain().getLocalizations().get(getMain().language, "info.affected_lamps_off").replace("%affected", "" + affected + ""));
            return true;
        }
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(getMain().getLocalizations().get(getMain().language, "error.console_use"));
        return true;
    }
}
