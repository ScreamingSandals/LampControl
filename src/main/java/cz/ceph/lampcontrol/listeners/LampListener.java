package cz.ceph.lampcontrol.listeners;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class LampListener implements Listener {

    @EventHandler
    public void onRedstoneBlockChange(BlockPhysicsEvent event) {
        Material material = event.getBlock().getType();

        if (material == Material.REDSTONE_LAMP && !LampControl.getMain().cachedRedstoneMaterials.contains(event.getChangedType())) {
            if (material.toString().contains("_PRESSURE_PLATE") || material.toString().contains("_BUTTON") &&
                    !LampControl.getMain().cachedBooleanValues.get("enable-redstone")) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }
}