package cz.ceph.lampcontrol.events;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.utils.ChatWriter;
import cz.ceph.lampcontrol.utils.SoundPlayer;
import cz.ceph.lampcontrol.workers.GetBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by SiOnzee on 17.07.2016.
 */

public class ReflectPlayerInteractEvent implements ReflectEvent.Callback {

    private LampControl plugin;

    public ReflectPlayerInteractEvent(LampControl plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(Event instance) {
        PlayerInteractEvent e = (PlayerInteractEvent) instance;

        /*
         * All about lamps here
         */
        Player player = e.getPlayer();

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (player.isSneaking()) return;

        if (getMain().cachedBooleanValues.get("manage-op") && e.getPlayer().isOp() || checkPermissions(player, "lampcontrol.hand")) {
            if (player.getItemInHand() == null || !player.getItemInHand().getType().equals(Material.AIR) && !player.getItemInHand().getType().equals(getMain().lampTool)) {
                return;
            }
        } else {
            if (player.getItemInHand() == null || !player.getItemInHand().getType().equals(getMain().lampTool)) {
                return;
            }
        }

        if (GetBlock.vGetLamp(true, e)) {
            if (getMain().cachedBooleanValues.get("use-permissions") && !checkPermissions(player, "lampcontrol.use"))
                return;

            if (!getMain().cachedBooleanValues.get("manage-lamps")) return;

            e.setCancelled(true);

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            plugin.getSwitchBlock().initWorld(b.getWorld());
            plugin.getSwitchBlock().switchLamp(b, false);

            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(b, blockState, b, new ItemStack(GetBlock.vGetLamp(false, b)), player, true);
            Bukkit.getPluginManager().callEvent(blockPlaceEvent);

            if (blockPlaceEvent.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(b, false);
                e.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                return;
            }

            if (getMain().cachedBooleanValues.get("use-items")) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            SoundPlayer.play(e.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);

        } else if (GetBlock.vGetLamp(false, e)) {

            e.setCancelled(true);

            if (getMain().cachedBooleanValues.get("use-permissions") && !e.getPlayer().hasPermission("lampcontrol.use"))
                return;

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            plugin.getSwitchBlock().initWorld(b.getWorld());
            plugin.getSwitchBlock().switchLamp(b, true);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(GetBlock.vGetLamp(true, b)), e.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(b, true);
                e.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                return;
            }

            if (getMain().cachedBooleanValues.get("use-items")) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            SoundPlayer.play(e.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 1F);
        }
        /*
         * End of lamps section
         */

        /*
         * All about rails here
         */
        else if (e.getClickedBlock().getType().equals(Material.POWERED_RAIL)) {
            if (getMain().cachedBooleanValues.get("use-permissions") && !e.getPlayer().hasPermission("lampcontrol.use"))
                return;

            if (!getMain().cachedBooleanValues.get("manage-rails")) return;

            e.setCancelled(true);

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();
            int data = (int) b.getData();

            if (data < 7) {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);
                if (checkBuildPerms.isCancelled()) {
                    e.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                    return;
                } else {
                    BlockFace[] sides = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
                    for (BlockFace side : sides) {
                        innerForEach:
                        for (int i = 1; i < 9; i++) {
                            Block railBlock = b.getRelative(side, i);
                            int railData = (int) railBlock.getData();

                            if (railBlock.getType().equals(Material.POWERED_RAIL) && railData < 7) {
                                plugin.getSwitchBlock().initWorld(railBlock.getWorld());
                                plugin.getSwitchBlock().switchRail(railBlock, true);
                            } else {
                                break innerForEach;
                            }
                        }
                    }
                    plugin.getSwitchBlock().initWorld(b.getWorld());
                    plugin.getSwitchBlock().switchRail(b, true);
                }

                if (getMain().cachedBooleanValues.get("use-item")) {
                    ItemStack item = e.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    e.getPlayer().setItemInHand(null);
                    e.getPlayer().updateInventory();
                }

                SoundPlayer.play(e.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 1F);
            } else {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);

                if (checkBuildPerms.isCancelled()) {
                    e.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                    return;
                } else {
                    plugin.getSwitchBlock().initWorld(b.getWorld());
                    plugin.getSwitchBlock().switchRail(b, false);
                }

                if (getMain().cachedBooleanValues.get("use-items")) {
                    ItemStack item = e.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    e.getPlayer().setItemInHand(null);
                }

                SoundPlayer.play(e.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
            }
        }

        /*
         * End of rails section
         */


    }

    private boolean checkPermissions(Player p, String permission) {
        return p.hasPermission(permission);
    }
}
