/*
	Code has been adapted from jacklink01's LampControl.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.lampcontrol.events;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Granik24 on 17.07.2016.
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

        // Lamps section
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (e.getPlayer().isSneaking()) return;

        if (LampControl.opUsesHand && e.getPlayer().isOp() || e.getPlayer().hasPermission("lampcontrol.hand")) {
            if (e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().equals(Material.AIR) && !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)) {
                return;
            }
        } else {
            if (e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)) {
                return;
            }
        }

        if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
            if (LampControl.usePermissions && !e.getPlayer().hasPermission("lampcontrol.use")) return;

            if (!LampControl.toggleLamps) return;

            e.setCancelled(true);

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            plugin.getSwitchBlock().initWorld(b.getWorld());
            plugin.getSwitchBlock().switchLamp(b, false);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_OFF), e.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(b, false);
                e.getPlayer().sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
                return;
            }

            if (LampControl.takeItemOnUse) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            playSound(e.getClickedBlock().getLocation(), 0.5F, 0F);
        } else if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) {

            e.setCancelled(true);

            if (LampControl.usePermissions && !e.getPlayer().hasPermission("lampcontrol.use")) return;

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            plugin.getSwitchBlock().initWorld(b.getWorld());
            plugin.getSwitchBlock().switchLamp(b, true);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                plugin.getSwitchBlock().switchLamp(b, true);
                e.getPlayer().sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
                return;
            }

            if (LampControl.takeItemOnUse) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            playSound(e.getClickedBlock().getLocation(), 0.5F, 1F);
        }

        // Rails section
        else if (e.getClickedBlock().getType().equals(Material.POWERED_RAIL)) {
            if (LampControl.usePermissions && !e.getPlayer().hasPermission("lampcontrol.use")) return;

            if (!LampControl.controlRails) return;

            e.setCancelled(true);

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();
            int data = (int) b.getData();

            if (data < 7) {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);
                if (checkBuildPerms.isCancelled()) {
                    e.getPlayer().sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
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

                if (LampControl.takeItemOnUse) {
                    ItemStack item = e.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    e.getPlayer().setItemInHand(null);
                    e.getPlayer().updateInventory();
                }

                playSound(e.getClickedBlock().getLocation(), 0.5F, 1.0F);
            } else {
                BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.POWERED_RAIL), e.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(checkBuildPerms);

                if (checkBuildPerms.isCancelled()) {
                    e.getPlayer().sendMessage(MessagesManager.PREFIX + MessagesManager.NO_PERMS.toString());
                    return;
                } else {
                    plugin.getSwitchBlock().initWorld(b.getWorld());
                    plugin.getSwitchBlock().switchRail(b, false);
                }

                if (LampControl.takeItemOnUse) {
                    ItemStack item = e.getPlayer().getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    e.getPlayer().setItemInHand(null);
                }

                playSound(e.getClickedBlock().getLocation(), 0.5F, 0F);
            }
        }

    }

    // 1.7 - 1.10 sounds
    private void playSound(Location loc, float v, float v1) {
        Sound[] sounds = Sound.values();
        Sound correctSound = null;
        for (Sound s : sounds) {
            if (s.toString().equalsIgnoreCase("ui_button_click"))
                correctSound = s;
            else if (s.toString().equalsIgnoreCase("click"))
                correctSound = s;
            if (correctSound != null)
                break;
        }

        if (correctSound == null) {
            System.out.println(MessagesManager.PREFIX + "Sound not found! Contact developer for help.");
            Arrays.stream(sounds).forEach(sound -> System.out.println(sound.toString()));
            return;
        }

        loc.getWorld().playSound(loc, correctSound, v, v1);
    }
}
