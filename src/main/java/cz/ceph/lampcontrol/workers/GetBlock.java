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
            if (light) {
                Lightable lightable = (Lightable) block.getBlockData();

                result = lightable.isLit();
            } else {
                Lightable lightable = (Lightable) block.getBlockData();
                result = !lightable.isLit();
            }
        }

        return result;
    }

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
}
