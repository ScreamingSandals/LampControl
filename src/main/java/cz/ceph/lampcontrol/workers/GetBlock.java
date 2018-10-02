package cz.ceph.lampcontrol.workers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.player.PlayerInteractEvent;

import static cz.ceph.lampcontrol.LampControl.simpleVersion;

/**
 * Created by iamceph on 02.10.2018.
 */
public class GetBlock {

    /* This vGetLamp method depends on Minecraft version
     * This vGetLamp method depends on
      *
      * vGetLamp is used to check if lamp is ON or OFF
      * Check is different by Minecraft version*/
    public static Boolean vGetLamp(Boolean light, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        BlockData blockData = block.getBlockData();
        boolean result;

        if (simpleVersion && blockData instanceof Lightable) {
            Lightable lightable = (Lightable) block.getBlockData();
            block.getType().compareTo(Material.REDSTONE_LAMP);

            result = light && lightable.isLit();

        } else {
            result = light && block.getType().equals(Material.getMaterial("REDSTONE_LAMP_ON"));
        }
        return result;
    }

    /* This vGetLamp method depends on Minecraft version
     * This vGetLamp method returns Lamp block depends on version
      *
      * vGetLamp is used to check if lamp is ON or OFF
      * Check is different by Minecraft version*/
    public static Material vGetLamp(boolean light, Block block) {
        Material mat;
        BlockData blockData = block.getBlockData();

        if (simpleVersion && blockData instanceof Lightable) {
            Lightable lightable = (Lightable) block.getBlockData();
            block.setType(Material.REDSTONE_LAMP);

            if (light) {
                lightable.setLit(true);
                block.setBlockData(lightable);

                mat = block.getType();
            } else {
                lightable.setLit(false);
                block.setBlockData(lightable);

                mat = block.getType();
            }

        } else {
            if (light) {
                block.setType(Material.getMaterial("REDSTONE_LAMP_ON"));

                mat = block.getType();

            } else {
                block.setType(Material.getMaterial("REDSTONE_LAMP_OFF"));

                mat = block.getType();
            }
        }

        return mat;
    }
}
