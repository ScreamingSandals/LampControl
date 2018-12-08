package cz.ceph.lampcontrol.events;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.utils.ChatWriter;
import cz.ceph.lampcontrol.utils.SoundPlayer;
import cz.ceph.lampcontrol.workers.GetBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by SiOnzee on 09.12.2018
 */

public class ReflectPlayerInteractEvent implements ReflectEvent.Callback {

    private LampControl plugin;

    public ReflectPlayerInteractEvent(LampControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Event instance) {
        PlayerInteractEvent event = (PlayerInteractEvent) instance;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block.getType().equals(Material.REDSTONE_LAMP)) {
            if (!checkPermissions(event) && checkCachedValues("control-lamps") && checkHandItem(event)) return;
            event.setCancelled(true);

            if (GetBlock.getLampStatus(false, block)) {
                BlockState blockState = block.getState();

                plugin.getSwitchBlock().initWorld(block.getWorld());
                plugin.getSwitchBlock().switchLamp(block, true);

                BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, blockState, block, new ItemStack(Material.REDSTONE_LAMP), player, true, EquipmentSlot.HAND);
                Bukkit.getPluginManager().callEvent(blockPlaceEvent);

                if (blockPlaceEvent.isCancelled()) {
                    plugin.getSwitchBlock().switchLamp(block, false);
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
            } else {
                BlockState blockState = block.getState();

                plugin.getSwitchBlock().initWorld(block.getWorld());
                plugin.getSwitchBlock().switchLamp(block, false);

                BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, blockState, block, new ItemStack(Material.REDSTONE_LAMP), player, true, EquipmentSlot.HAND);
                Bukkit.getPluginManager().callEvent(blockPlaceEvent);

                if (blockPlaceEvent.isCancelled()) {
                    plugin.getSwitchBlock().switchLamp(block, true);
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
            }

        } else if (block.getType().equals(Material.POWERED_RAIL)) {
            if (!checkPermissions(event) && checkCachedValues("control-rails") && checkHandItem(event)) return;
            event.setCancelled(true);

            if (GetBlock.getRailStatus(false, event)) {
                BlockState blockState = block.getState();

                plugin.getSwitchBlock().initWorld(block.getWorld());
                plugin.getSwitchBlock().switchRail(block, true);

                BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, blockState, block, new ItemStack(Material.POWERED_RAIL), player, true, EquipmentSlot.HAND);
                Bukkit.getPluginManager().callEvent(blockPlaceEvent);

                if (blockPlaceEvent.isCancelled()) {
                    plugin.getSwitchBlock().switchRail(block, false);
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
            } else {
                BlockState blockState = block.getState();

                plugin.getSwitchBlock().initWorld(block.getWorld());
                plugin.getSwitchBlock().switchRail(block, false);

                BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, blockState, block, new ItemStack(Material.POWERED_RAIL), player, true, EquipmentSlot.HAND);
                Bukkit.getPluginManager().callEvent(blockPlaceEvent);

                if (blockPlaceEvent.isCancelled()) {
                    plugin.getSwitchBlock().switchRail(block, true);
                    event.getPlayer().sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                }

                SoundPlayer.play(event.getClickedBlock().getLocation(), SoundPlayer.success(), 0.5F, 0F);
            }
        }
    }

    private boolean checkPermissions(PlayerInteractEvent event) {
        return getMain().cachedBooleanValues.get("enable-permissions") && !event.getPlayer().hasPermission("lampcontrol.use");
    }

    private boolean checkCachedValues(String value) {
        return getMain().cachedBooleanValues.get(value);
    }

    private boolean checkHandItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || (!player.isSneaking())) {
            if (checkCachedValues("control-OP") && player.getItemOnCursor().getType().equals(Material.AIR) && player.getItemOnCursor().getType().equals(getMain().lampTool)) {
                return true;
            }
        } else {
            if (player.getItemOnCursor() == null || player.getItemOnCursor().getType().equals(getMain().lampTool)) {
                return true;
            }
        }
        return false;
    }
}
