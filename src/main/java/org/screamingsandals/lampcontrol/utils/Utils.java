package org.screamingsandals.lampcontrol.utils;

import org.bukkit.Bukkit;

/**
 * @author ScreamingSandals team
 */
public class Utils {
    public static String getNMSVersion(){
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }
}
