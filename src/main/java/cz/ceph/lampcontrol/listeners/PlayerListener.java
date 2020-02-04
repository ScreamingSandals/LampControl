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

/**
 * @author ScreamingSandals team
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || block == null) {
            return;
        }

        if (player.hasPermission("lampcontrol.rightclick")) {
            final Material material = block.getType();
            if (Main.getEnvironment().isLightable(material)) {
                if (BlockStatus.isLit(block)) {
                    Main.getSwitchBlock().setLit(block, false);
                } else {
                    Main.getSwitchBlock().setLit(block, true);
                }
            } else if (Main.getEnvironment().isPowerable(material)) {
                if (BlockStatus.isPowered(block)) {
                    Main.getSwitchBlock().setPowered(block, false);
                } else {
                    Main.getSwitchBlock().setPowered(block, true);
                }
            }
        }

    }
}
