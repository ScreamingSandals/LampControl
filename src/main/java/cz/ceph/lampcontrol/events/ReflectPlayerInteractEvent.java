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
        PlayerInteractEvent event = (PlayerInteractEvent) instance;

        /*
         * All about lamps here
         */
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (player.isSneaking()) return;

        if (getMain().cachedBooleanValues.get("manage-op") && event.getPlayer().isOp() || checkPermissions(player, "lampcontrol.hand")) {
            if (player.getItemInHand() == null || !player.getItemInHand().getType().equals(Material.AIR) && !player.getItemInHand().getType().equals(getMain().lampTool)) {
                return;
            }
        } else {
            if (player.getItemInHand() == null || !player.getItemInHand().getType().equals(getMain().lampTool)) {
                return;
            }
        }

        if (GetBlock.vGetLamp(true, event)) {
            if (getMain().cachedBooleanValues.get("use-permissions") && !checkPermissions(player, "lampcontrol.use"))
                return;

            if (!getMain().cachedBooleanValues.get("manage-lamps")) return;

            event.setCancelled(true);

            Block block = event.getClickedBlock();
            BlockState blockState = block.getState();

            plugin.getSwitchBlock().initWorld(block.getWorld());
            plugin.getSwitchBlock().switchLamp(block, false);

            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, blockState, block, new ItemStack(GetBlock.vGetLamp(false, block)), player, true);
            Bukkit.getPluginManager().callEvent(blockPlaceEvent);

            if (blockPlaceEvent.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(block, false);
                event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                return;
            }

            if (getMain().cachedBooleanValues.get("use-items")) {
                ItemStack item = event.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                event.getPlayer().setItemInHand(null);
            }

            SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);

        } else if (GetBlock.vGetLamp(false, event)) {

            event.setCancelled(true);

            if (getMain().cachedBooleanValues.get("use-permissions") && !event.getPlayer().hasPermission("lampcontrol.use"))
                return;

            Block b = event.getClickedBlock();
            BlockState blockState = b.getState();

            plugin.getSwitchBlock().initWorld(b.getWorld());
            plugin.getSwitchBlock().switchLamp(b, true);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(GetBlock.vGetLamp(true, b)), event.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(b, true);
                event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                return;
            }

            if (getMain().cachedBooleanValues.get("use-items")) {
                ItemStack item = event.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                event.getPlayer().setItemInHand(null);
            }

            SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 1F);
        }
        /*
         * End of lamps section
         */

        /*
         * All about rails here
         */
        else if (event.getClickedBlock().getType().equals(Material.POWERED_RAIL)) {
            if (getMain().cachedBooleanValues.get("use-permissions") && !event.getPlayer().hasPermission("lampcontrol.use"))
                return;

            if (!getMain().cachedBooleanValues.get("manage-rails")) return;

            event.setCancelled(true);

            Block b = event.getClickedBlock();
            BlockState blockState = b.getState();
            int data = (int) b.getData();

            if (data < 7) {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), event.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);
                if (checkBuildPerms.isCancelled()) {
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
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
                                //plugin.getSwitchBlock().switchRail(railBlock, true);
                            } else {
                                break innerForEach;
                            }
                        }
                    }
                    plugin.getSwitchBlock().initWorld(b.getWorld());
                    //plugin.getSwitchBlock().switchRail(b, true);
                }

                if (getMain().cachedBooleanValues.get("use-item")) {
                    ItemStack item = event.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().setItemInHand(null);
                    event.getPlayer().updateInventory();
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 1F);
            } else {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), event.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);

                if (checkBuildPerms.isCancelled()) {
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localizations.get("error.no_permissions")));
                    return;
                } else {
                    plugin.getSwitchBlock().initWorld(b.getWorld());
                    //plugin.getSwitchBlock().switchRail(b, false);
                }

                if (getMain().cachedBooleanValues.get("use-items")) {
                    ItemStack item = event.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().setItemInHand(null);
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
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
