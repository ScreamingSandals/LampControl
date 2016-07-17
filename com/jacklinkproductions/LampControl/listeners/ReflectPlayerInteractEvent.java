package com.jacklinkproductions.LampControl.listeners;

import com.jacklinkproductions.LampControl.Main;
import com.jacklinkproductions.LampControl.ReflectEvent;
import com.jacklinkproductions.LampControl.SwitchBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
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

    Main plugin;

    public ReflectPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Event instance) {
        PlayerInteractEvent e = (PlayerInteractEvent) instance;

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (e.getPlayer().isSneaking()) return;

        if (Main.opUsesHand && e.getPlayer().isOp() || e.getPlayer().hasPermission("lampcontrol.hand")) {
            if (e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().equals(Material.AIR) && !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)) {
                return;
            }
        } else {
            if (e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial)) {
                return;
            }
        }

        if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
            if (Main.usePermissions && !e.getPlayer().hasPermission("lampcontrol.use")) return;

            if (!Main.toggleLamps) return;

            e.setCancelled(true);

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            SwitchBlock.switchLamp(b, false);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_OFF), e.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                SwitchBlock.switchLamp(b, false);
                e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here!");
                return;
            }

            if (Main.takeItemOnUse) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            playSound(e.getClickedBlock().getLocation(), 0.5F, 0F);
        } else if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) {

            e.setCancelled(true);

            if (Main.usePermissions && !e.getPlayer().hasPermission("lampcontrol.use")) return;

            Block b = e.getClickedBlock();
            BlockState blockState = b.getState();

            SwitchBlock.switchLamp(b, true);

            BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
            Bukkit.getPluginManager().callEvent(checkBuildPerms);

            if (checkBuildPerms.isCancelled()) {
                SwitchBlock.switchLamp(b, true);
                e.getPlayer().sendMessage(ChatColor.RED + Main.prefix + "You don't have permissions to build here!");
                return;
            }

            if (Main.takeItemOnUse) {
                ItemStack item = e.getPlayer().getItemInHand();
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().setItemInHand(null);
            }

            playSound(e.getClickedBlock().getLocation(), 0.5F, 1F);
        }

    }

    public void playSound(Location loc, float v, float v1) {
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
            System.out.println("Sound not found!");
            Arrays.stream(sounds).forEach(sound -> System.out.println(sound.toString()));
            return;
        }

        loc.getWorld().playSound(loc, correctSound, v, v1);
    }
}
