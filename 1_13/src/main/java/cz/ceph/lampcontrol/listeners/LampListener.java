package cz.ceph.lampcontrol.listeners;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onRedstoneBlockChange(BlockRedstoneEvent event) {
        /*Material lampBlock = event.getBlock().getType();

        LampControl.debug.info("Material is: " + lampBlock);
        if (lampBlock.equals(Material.REDSTONE_LAMP) || lampBlock.equals(Material.POWERED_RAIL) && !getMain().cachedBooleanValues.get("enable-plates")) {
            LampControl.debug.info("In list is: " + getMain().cachedMaterials.contains(event.getBlock().getType()));
            LampControl.debug.info(getMain().cachedMaterials.toString());
            if (getMain().cachedMaterials.contains(event.getBlock().getType()))
            event.setNewCurrent(100);
        } */
    }
}