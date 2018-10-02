package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Bukkit;

/**
 * Created by iamceph on 01.10.2018.
 */

public class VersionChecker {

    public static boolean checkVersion() {
        return LampControl.bukkitVersion.equals("1.13");
    }

    public static String getBukkitVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
