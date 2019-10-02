package cz.ceph.lampcontrol.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import cz.ceph.lampcontrol.utils.SoundPlayer;
import cz.ceph.lampcontrol.utils.GetBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

import static cz.ceph.lampcontrol.LampControl.getMain;
import static misat11.lib.lang.I18n.i18n;

public class OffCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
            //TODO: only player can use this
        }

        Player player = (Player) commandSender;
        WorldEditPlugin worldEdit;
        worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        if (worldEdit == null) {
            player.sendMessage(i18n("error.no_worldedit"));
            SoundPlayer.play(player.getLocation(), SoundPlayer.fail(), 0.5F, 0F);
            return true;
        } else {
            int affected = 0;
            boolean checkForSelection = false;

            BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(player);
            LocalSession localSession = worldEdit.getSession(player);
            Region region;

            try {
                region = localSession.getSelection(bukkitPlayer.getWorld());

                if (region == null) {
                    player.sendMessage(i18n("error.no_selection"));
                    SoundPlayer.play(player.getLocation(), SoundPlayer.fail(), 0.5F, 0F);
                    return true;
                }

                if (!(region instanceof CuboidRegion)) {
                    checkForSelection = true;
                }

                BlockVector3 min = region.getMinimumPoint();
                BlockVector3 max = region.getMaximumPoint();

                Location minLoc = new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
                Location maxLoc = new Location(player.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());

                getMain().getSwitchBlock().initWorld(minLoc.getWorld());

                for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
                    for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++)
                        for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                            BlockVector3 vectorLocation = BlockVector3.at(min.getX(), min.getY(), min.getZ());
                            Location location = new Location(minLoc.getWorld(), x, y, z);

                            if (!checkForSelection || region.contains(vectorLocation)) {
                                Block block = minLoc.getWorld().getBlockAt(location);

                                if (GetBlock.getLampStatus(true, block)) {
                                    try {
                                        getMain().getSwitchBlock().switchLamp(block, false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    affected++;
                                } else {
                                    affected = 0;
                                }
                            }
                        }
                }

            } catch (IncompleteRegionException | NullPointerException e) {
                player.sendMessage(i18n("error.no_selection"));
            }

            if (affected < 1) {
                player.sendMessage(i18n("info.no_lamps_affecetd"));
                SoundPlayer.play(player.getLocation(), SoundPlayer.fail(), 0.5F, 0F);
            } else
                player.sendMessage(i18n("info.affected_lamps_off").replace("%affected", "" + affected + ""));
            SoundPlayer.play(player.getLocation(), SoundPlayer.success(), 0.5F, 0F);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
