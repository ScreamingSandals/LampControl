package cz.ceph.lampcontrol.workers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;


/**
 * Created by iamceph on 01.10.2018.
 */
public class SwitchMaterial {

    /* This vLampSwitcher method depends on Minecraft version
     * This vLampSwitcher method returns Lamp block depends on version
      *
      * vLampSwitcher is used to select material by Minecraft version*/
    public static void lampSwitcher(Boolean light, Block block) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Lightable) {
            Lightable lightable = (Lightable) block.getBlockData();
            block.setType(Material.REDSTONE_LAMP);

            if (light) {
                lightable.setLit(true);
                block.setBlockData(lightable);
            } else {
                lightable.setLit(false);
                block.setBlockData(lightable);
            }
        }
    }
}
