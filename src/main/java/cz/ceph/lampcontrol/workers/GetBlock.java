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

    public static Boolean getLamp(Boolean light, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        BlockData blockData = block.getBlockData();

        boolean result = false;

        if (blockData instanceof Lightable) {
            LampControl.debug.info("GetBlock: block instanceof light");
            if (light) {
                LampControl.debug.info("GetBlock:  light = true");
                Lightable lightable = (Lightable) block.getBlockData();

                result = lightable.isLit();
            } else {
                LampControl.debug.info("GetBlock:  light = false");
                Lightable lightable = (Lightable) block.getBlockData();
                result = !lightable.isLit();
            }
        } else {
            LampControl.debug.info("GetBlock:  not instance of light");
        }

        return result;
    }

    public static Material getLamp(boolean light, Block block) {
        Material mat;
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Lightable) {
            LampControl.debug.info("GetBlockMat: block instanceof light");
            if (light) {
                LampControl.debug.info("GetBlockMat:  light = true");
                Lightable lightable = (Lightable) block.getBlockData();

                block.getType().compareTo(Material.REDSTONE_LAMP);
                lightable.setLit(true);
                block.setBlockData(lightable);

                mat = block.getType();
            } else {
                LampControl.debug.info("GetBlockMat:  light = false");
                mat = block.getType();
            }
        } else {
            LampControl.debug.info("GetBlockMat:  not instance of light");
            mat = block.getType();
        }

        return mat;
    }
}
