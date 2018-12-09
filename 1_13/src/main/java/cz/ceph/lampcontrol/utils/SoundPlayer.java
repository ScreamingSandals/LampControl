package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.Arrays;

import static cz.ceph.lampcontrol.LampControl.bukkitVersion;

/**
 * Created by iamceph on 20.06.2018.
 */

public class SoundPlayer {

    public static void play(Location loc, String sound, float v, float v1) {
        loc.getWorld().playSound(loc, sound, v, v1);
    }

    public static String success() {
        return "ui.button.click";

    }

    public static String fail() {
        return "block.glass.break";
    }

}
