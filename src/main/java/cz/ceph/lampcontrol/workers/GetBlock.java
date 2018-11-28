package cz.ceph.lampcontrol.workers;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by iamceph on 02.10.2018.
 */
public class GetBlock {

    public static boolean getLampStatus(boolean light, Block block) {
        if (getBlock(block)) {
            if (light) {
                Lightable lightable = (Lightable) block.getBlockData();
                return lightable.isLit();

            } else {
                Lightable lightable = (Lightable) block.getBlockData();
                return !lightable.isLit();
            }
        } else {
            return false;
        }
    }

    public static boolean getLampStatus(boolean light, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (getBlock(block)) {
            if (light) {
                LampControl.debug.info("getLampStatus, light true");
                Lightable lightable = (Lightable) block.getBlockData();
                LampControl.debug.info("lightable is " + lightable.isLit());
                return lightable.isLit();


            } else {
                Lightable lightable = (Lightable) block.getBlockData();
                LampControl.debug.info("lightable is " + lightable.isLit());
                return !lightable.isLit();
            }
        } else {
            return false;
        }
    }

    private static boolean getBlock(Block block) {
        Material blockMat = block.getType();

        return blockMat == Material.REDSTONE_LAMP;
    }
}
