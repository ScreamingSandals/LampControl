package cz.ceph.lampcontrol.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onRedstoneBlockChange(BlockPhysicsEvent event) {
        boolean lampItem;
        Block block = event.getBlock();
        lampItem = block.getType().equals(Material.REDSTONE_LAMP);

        if (lampItem || !getMain().cachedBooleanValues.get("enable-plates") && !getMain().cachedBooleanValues.get("control-redstone")) {
            event.setCancelled(false);
        }
    }
}