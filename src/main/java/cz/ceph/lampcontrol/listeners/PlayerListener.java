package cz.ceph.lampcontrol.listeners;

import cz.ceph.lampcontrol.Main;
import cz.ceph.lampcontrol.world.BlockStatus;
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
            if (changeBlock(block)) {
                Main.withdrawPlayer(player);
            }
            return;
        }

        if (player.hasPermission("lampcontrol.rightclick")) {
            changeBlock(block);
        }

    }

    private boolean changeBlock(Block block) {
        final Material material = block.getType();
        if (Main.getEnvironment().isLightable(material)) {
            if (BlockStatus.isLit(block)) {
                Main.getSwitchBlock().setLit(block, false);
                return false;
            } else {
                Main.getSwitchBlock().setLit(block, true);
                return true;
            }
        } else if (Main.getEnvironment().isPowerable(material)) {
            if (BlockStatus.isPowered(block)) {
                Main.getSwitchBlock().setPowered(block, false);
                return false;
            } else {
                Main.getSwitchBlock().setPowered(block, true);
                return true;
            }
        }
        return false;
    }
}
