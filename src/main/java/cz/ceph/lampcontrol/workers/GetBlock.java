package cz.ceph.lampcontrol.workers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by iamceph on 02.10.2018.
 */
public class GetBlock {

    public static Material getLamp(boolean light, Block block) {
        Material mat;
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Lightable) {
            if (light) {
                Lightable lightable = (Lightable) block.getBlockData();

                block.getType().compareTo(Material.REDSTONE_LAMP);
                lightable.setLit(true);
                block.setBlockData(lightable);

                mat = block.getType();
            } else {
                mat = block.getType();
            }
        } else {
            mat = block.getType();
        }
        return mat;
    }

    public static boolean getLampStatus(boolean light, Block block) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Lightable) {
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
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Lightable) {
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
}
