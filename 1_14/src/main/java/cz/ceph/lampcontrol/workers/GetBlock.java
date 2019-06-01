package cz.ceph.lampcontrol.workers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by iamceph on 02.10.2018.
 */
public class GetBlock {

    public static boolean getLampStatus(boolean light, Block block) {
        if (getLampBlock(block)) {
            Lightable lightable = (Lightable) block.getBlockData();

            if (light) {
                return lightable.isLit();

            } else {
                return !lightable.isLit();
            }
        } else {
            return false;
        }
    }

    public static boolean getRailStatus(boolean power, Block block) {
        if (getRailBlock(block)) {
            Powerable powerable = (Powerable) block.getBlockData();

            if (power) {
                return powerable.isPowered();


            } else {
                return !powerable.isPowered();
            }
        } else {
            return false;
        }
    }

    public static boolean getRailStatus(boolean power, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (getRailBlock(block)) {
            Powerable powerable = (Powerable) block.getBlockData();

            if (power) {
                return powerable.isPowered();


            } else {
                return !powerable.isPowered();
            }
        } else {
            return false;
        }
    }

    private static boolean getLampBlock(Block block) {
        Material blockMat = block.getType();

        return blockMat == Material.REDSTONE_LAMP;
    }

    private static boolean getRailBlock(Block block) {
        Material blockMat = block.getType();

        return blockMat == Material.POWERED_RAIL;
    }
}
