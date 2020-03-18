package org.screamingsandals.lampcontrol.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.screamingsandals.lampcontrol.Main;
import org.screamingsandals.lampcontrol.api.BlockInfo;
import org.screamingsandals.lampcontrol.api.events.PlayerChangeBlockStateEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @author ScreamingSandals team
 */
public class PlayerListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getDataManager().createPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        final ItemStack itemStack = event.getItem();

        if (event.getHand() != EquipmentSlot.HAND
                || event.getAction() != Action.RIGHT_CLICK_BLOCK
                || block == null) {
            return;
        }

        if (itemStack != null
                && itemStack.getType() == Main.getBaseConfig().getMaterial("vault.item")) {
            if (processChange(player, block)) {
                Main.withdrawPlayer(player);
            }
            return;
        }

        if (player.hasPermission("lampcontrol.right-click")) {
            processChange(player, block);
        }

    }

    private boolean processChange(Player player, Block block) {
        final Material material = block.getType();
        if (BlockInfo.isLightable(material)) {
            if (BlockInfo.isLit(block)) {
                if (callEvent(player, block, PlayerChangeBlockStateEvent.Type.LIT, PlayerChangeBlockStateEvent.Action.OFF)) {
                    Main.getSwitchBlock().setLit(block, false);
                    return false;
                }
            } else {
                if (callEvent(player, block, PlayerChangeBlockStateEvent.Type.LIT, PlayerChangeBlockStateEvent.Action.ON)) {
                    Main.getSwitchBlock().setLit(block, true);
                    return true;
                }
            }
        } else if (BlockInfo.isPowerable(material)) {
            if (BlockInfo.isPowered(block)) {
                if (callEvent(player, block, PlayerChangeBlockStateEvent.Type.POWER, PlayerChangeBlockStateEvent.Action.OFF)) {
                    Main.getSwitchBlock().setPowered(block, false);
                    return false;
                }
            } else {
                if (callEvent(player, block, PlayerChangeBlockStateEvent.Type.POWER, PlayerChangeBlockStateEvent.Action.ON)) {
                    Main.getSwitchBlock().setPowered(block, true);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean callEvent(Player player, Block block, PlayerChangeBlockStateEvent.Type type, PlayerChangeBlockStateEvent.Action action) {
        PlayerChangeBlockStateEvent playerChangeBlockStateEvent = new PlayerChangeBlockStateEvent(player, block, type, action);
        playerChangeBlockStateEvent.callEvent();

        return !playerChangeBlockStateEvent.isCancelled();
    }
}
