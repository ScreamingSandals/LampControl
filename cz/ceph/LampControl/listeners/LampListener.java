package cz.ceph.lampcontrol.listeners;


import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;


public class LampListener implements Listener {
    private LampControl plugin;

    public LampListener(LampControl p) {
        this.plugin = p;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lamp = e.getBlock().getType().equals(Material.REDSTONE_LAMP_ON);

        if (!getMain().cachedBooleanValues.get("woodenPlateControl") || !getMain().cachedBooleanValues.get("stibePlateControl") || lamp && !plugin.containMaterials(e.getChangedType())) {
                e.setCancelled(true);
        }
    }
}