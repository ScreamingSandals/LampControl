package cz.ceph.lampcontrol.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lampBlock = e.getBlock().getType().equals(Material.REDSTONE_LAMP_ON);

        if (lampBlock ||!getMain().cachedBooleanValues.get("enable-plates") && !getMain().cachedBooleanValues.get("controlRedstone")) {
            e.setCancelled(true);
        }
    }
}