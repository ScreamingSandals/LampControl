package cz.ceph.lampcontrol.commands;

import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import cz.ceph.lampcontrol.Main;
import cz.ceph.lampcontrol.world.BlockStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.screamingcommands.annotations.RegisterCommand;
import org.screamingsandals.lib.screamingcommands.api.BukkitCommand;
import org.screamingsandals.lib.screamingcommands.api.ICommand;

import java.util.List;

/**
 * @author ScreamingSandals team
 */
@RegisterCommand
public class OnCommand implements BukkitCommand {
    public OnCommand(Main main) {
    }

    @Override
    public boolean onPlayerCommand(Player player, ICommand iCommand, List<String> list) {
        WorldEditPlugin worldEdit;
        worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        if (worldEdit == null) {
            player.sendMessage(); //Error, no worldedit
        } else {
            int affected = 0;
            boolean checkForSelection = false;

            BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(player);
            LocalSession localSession = worldEdit.getSession(player);
            Region region;

            try {
                region = localSession.getSelection(bukkitPlayer.getWorld());

                if (region == null) {
                    player.sendMessage(); //no selection
                    return true;
                }

                if (!(region instanceof CuboidRegion)) {
                    checkForSelection = true;
                }

                BlockVector3 min = region.getMinimumPoint();
                BlockVector3 max = region.getMaximumPoint();
                Location minLoc = new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
                Location maxLoc = new Location(player.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());

                for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
                    for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++)
                        for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                            BlockVector3 vectorLocation = BlockVector3.at(min.getX(), min.getY(), min.getZ());
                            Location location = new Location(minLoc.getWorld(), x, y, z);

                            if (!checkForSelection || region.contains(vectorLocation)) {
                                Block block = minLoc.getWorld().getBlockAt(location);
                                if (BlockStatus.isLit(block)) {
                                    try {
                                        Main.getSwitchBlock().setLit(block, true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    affected++;
                                }
                            }
                        }
                }


            } catch (IncompleteRegionException | NullPointerException e) {
                player.sendMessage(); //no selection
            }


            if (affected < 1) {
                player.sendMessage(); //no lamps affected
            } else {
                player.sendMessage(); //affected lamps
            }
        }
        return true;
    }

    @Override
    public boolean onConsoleCommand (ConsoleCommandSender consoleCommandSender, ICommand
            iCommand, List < String > list){
        return false;
    }

    @Override
    public Iterable<String> onPlayerTabComplete (Player player, ICommand iCommand, List < String > list){
        return ImmutableSet.of();
    }

    @Override
    public Iterable<String> onConsoleTabComplete (ConsoleCommandSender consoleCommandSender, ICommand
            iCommand, List < String > list){
        return ImmutableSet.of();
    }

    @Override
    public @NotNull String getCommandName () {
        return "lampcontrol";
    }

    @Override
    public @NotNull String getSubCommandName () {
        return "on";
    }

    @Override
    public @NotNull String getPermission () {
        return "lampcontrol.command.on";
    }

    @Override
    public @NotNull String getDescription () {
        return "Truns on all lightable/powerable sources.";
    }

    @Override
    public @NotNull String getUsage () {
        return "/lampcontrol on";
    }
}
