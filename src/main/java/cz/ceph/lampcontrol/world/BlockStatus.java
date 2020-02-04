package cz.ceph.lampcontrol.world;

import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;

/**
 * @author ScreamingSandals team
 */
public class BlockStatus {

    public static boolean isLit(Block block) {
        Lightable lightable = (Lightable) block.getBlockData();
        return lightable.isLit();
    }

    public static boolean isPowered(Block block) {
        Powerable powerable = (Powerable) block.getBlockData();
        return powerable.isPowered();
    }
}
