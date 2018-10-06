package cz.ceph.lampcontrol.listeners;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.workers.GetBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock(BlockRedstoneEvent event) {
        boolean lampItem;
        Block block = event.getBlock();
        Material mat = block.getType();
        lampItem = block.getType().equals(Material.REDSTONE_LAMP);

        if (lampItem || !getMain().cachedBooleanValues.get("enable-plates") && !getMain().containMaterials(mat)) {
            LampControl.debug.info("Material " + mat + " is in list as " + getMain().cachedRedstoneMaterials.contains(mat));
            event.setNewCurrent(100);
        }
    }
}