package cz.ceph.lampcontrol.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;
import static cz.ceph.lampcontrol.LampControl.simpleVersion;

public class LampListener implements Listener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lampItem;

        if (simpleVersion) {
            lampItem = e.getBlock().getType().equals(Material.REDSTONE_LAMP);
        }
        else {
            lampItem = e.getBlock().getType().equals("");
        }

        if (!getMain().cachedBooleanValues.get("use-plates") || lampItem && !getMain().containMaterials(e.getChangedType())) {
            e.setCancelled(true);
        }
    }
}