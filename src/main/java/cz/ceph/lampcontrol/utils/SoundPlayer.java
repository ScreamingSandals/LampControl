package cz.ceph.lampcontrol.utils;

import org.bukkit.Location;

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
