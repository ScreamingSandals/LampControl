package cz.ceph.lampcontrol.workers;

import cz.ceph.lampcontrol.utils.VersionChecker;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.player.PlayerInteractEvent;

import static cz.ceph.lampcontrol.LampControl.simpleVersion;

/**
 * Created by iamceph on 01.10.2018.
 */
public class SwitchMaterial {

    /* This lamp switcher depends on Minecraft version */
    public static void vLampSwitcher(Boolean light, Block block) {

        if (simpleVersion) {
            Lightable lightable = (Lightable) block.getBlockData();
            block.setType(Material.REDSTONE_LAMP);

            if (light) {
                lightable.setLit(true);
                block.setBlockData(lightable);
            } else {
                lightable.setLit(false);
                block.setBlockData(lightable);
            }

        } else {
            if (light) {
                block.setType(Material.getMaterial("REDSTONE_LAMP_ON"));

            } else {
                block.setType(Material.getMaterial("REDSTONE_LAMP_OFF"));
            }
        }
    }
}
