package cz.ceph.lampcontrol.listeners;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.workers.GetBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        boolean lampItem;
        Block block = event.getBlock();
        lampItem = block.getType().equals(Material.REDSTONE_LAMP);

        if (lampItem || !getMain().cachedBooleanValues.get("use-plates") && !getMain().containMaterials(event.getChangedType())) {
            LampControl.debug.info("Material" + event.getChangedType() + "is in list as " + getMain().cachedRedstoneMaterials.contains(event.getChangedType()));
            event.setCancelled(true);
        }
    }
}