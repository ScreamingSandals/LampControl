package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.Arrays;

/**
 * Created by iamceph on 20.06.2018.
 */

public class SoundPlayer {

    public static void play(Location loc, String sound, float v, float v1) {
        loc.getWorld().playSound(loc, sound, v, v1);
    }

    public static String success() {
        return "click";

    }

    public static String fail() {
        return "sup";
    }

}
