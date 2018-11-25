package cz.ceph.lampcontrol.utils;

import org.bukkit.Bukkit;

/**
 * Created by iamceph on 01.10.2018.
 */

public class VersionChecker {

    public static String getBukkitVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
