package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.Arrays;

/**
 * Created by iamceph on 20.06.2018.
 */

public class PlaySound {

    public static void play(Location loc, String sound, float v, float v1) {
        checkSound(sound);
        loc.getWorld().playSound(loc, sound, v, v1);
    }

    private static void checkSound(String usedSound) {
        Sound[] sounds= Sound.values();
        Sound correctSound = null;

        for (Sound s : sounds) {
            if (s.toString().equalsIgnoreCase(usedSound))
                correctSound = s;
            else if (s.toString().equalsIgnoreCase("ui_button_click"))
                correctSound = s;
            else if (s.toString().equalsIgnoreCase("click"))
                correctSound = s;
            if (correctSound != null)
                break;
        }

        if (correctSound == null)
            LampControl.debug.warning("Sound not found!");
            Arrays.stream(sounds).forEach(sound -> System.out.println(sound.toString()));
    }

    public static String success() {
        return "click, ui_button_click";

    }

    public static String fail() {
        return "sup";
    }

}
