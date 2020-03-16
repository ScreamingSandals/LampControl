package cz.ceph.lampcontrol.commands;

import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import cz.ceph.lampcontrol.Main;
import cz.ceph.lampcontrol.api.BlockInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.screamingcommands.annotations.RegisterCommand;
import org.screamingsandals.lib.screamingcommands.api.BukkitCommand;
import org.screamingsandals.lib.screamingcommands.api.ICommand;

import java.util.List;

import static org.screamingsandals.lib.lang.I.mpr;

/**
 * @author ScreamingSandals team
 */
@RegisterCommand
public class OffCommand implements BukkitCommand {
    public OffCommand(Main main) {
    }

    @Override
    public boolean onPlayerCommand(Player player, ICommand iCommand, List<String> list) {
        if (!Main.isWorldEdit()) {
            mpr("commands.no-worldedit").send(player);
            return true;
        }

        int replaced = 0;
        BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(player);
        LocalSession localSession = Main.getWorldEdit().getSession(player);
        Region region;

        try {
            region = localSession.getSelection(bukkitPlayer.getWorld());
            if (!(region instanceof CuboidRegion)) {
                mpr("commands.no-selection").send(player);
                return true;
            }
        } catch (IncompleteRegionException ignored) {
            mpr("commands.no-selection").send(player);
            return true;
        }

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();
        Location minLoc = new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Location maxLoc = new Location(player.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());

        for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
            for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++) {
                for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                    BlockVector3 vectorLocation = BlockVector3.at(min.getX(), min.getY(), min.getZ());
                    Location location = new Location(minLoc.getWorld(), x, y, z);

                    if (region.contains(vectorLocation)) {
                        Block block = minLoc.getWorld().getBlockAt(location);
                        final Material material = block.getType();
                        if (BlockInfo.isLightable(material)
                                && BlockInfo.isLit(block)) {
                            Main.getSwitchBlock().setLit(block, false);
                            replaced++;
                        } else if (BlockInfo.isPowerable(material)
                                && BlockInfo.isPowered(block)) {
                            Main.getSwitchBlock().setPowered(block, false);
                            replaced++;
                        }
                    }
                }
            }
        }

        if (replaced < 1) {
            mpr("commands.not_replaced").send(player);
        } else {
            mpr("commands.replaced")
                    .replace("%count%", replaced)
                    .send(player);
        }
        return true;
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender consoleCommandSender, ICommand iCommand, List<String> list) {
        return false;
    }

    @Override
    public Iterable<String> onPlayerTabComplete(Player player, ICommand iCommand, List<String> list) {
        return ImmutableSet.of();
    }

    @Override
    public Iterable<String> onConsoleTabComplete(ConsoleCommandSender consoleCommandSender, ICommand
            iCommand, List<String> list) {
        return ImmutableSet.of();
    }

    @Override
    public @NotNull String getCommandName() {
        return "lampcontrol";
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "off";
    }

    @Override
    public @NotNull String getPermission() {
        return "lampcontrol.command.on";
    }

    @Override
    public @NotNull String getDescription() {
        return "Truns on all lightable/powerable sources.";
    }

    @Override
    public @NotNull String getUsage() {
        return "/lampcontrol on";
    }
}
