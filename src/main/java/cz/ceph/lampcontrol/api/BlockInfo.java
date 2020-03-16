package cz.ceph.lampcontrol.api;

import cz.ceph.lampcontrol.environment.Environment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;

/**
 * @author ScreamingSandals team
 */
public interface BlockInfo {
    static boolean isLit(Block block) {
        Lightable lightable = (Lightable) block.getBlockData();
        return lightable.isLit();
    }

    static boolean isPowered(Block block) {
        Powerable powerable = (Powerable) block.getBlockData();
        return powerable.isPowered();
    }

    static boolean isLightable(Material material) {
        for (Material mat : Environment.getInstance().getLIGHTABLE()) {
            if (mat == material) {
                return true;
            }
        }
        return false;
    }

    static boolean isPowerable(Material material) {
        for (Material mat : Environment.getInstance().getPOWERABLE()) {
            if (mat == material) {
                return true;
            }
        }
        return false;
    }
}
