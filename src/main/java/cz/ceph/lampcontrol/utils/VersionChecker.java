package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Bukkit;

import static cz.ceph.lampcontrol.LampControl.bukkitVersion;

/**
 * Created by iamceph on 01.10.2018.
 */

public class VersionChecker {

    public static boolean checkVersion() {
        return !(bukkitVersion.contains("1_13"));
    }

    public static String getBukkitVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
