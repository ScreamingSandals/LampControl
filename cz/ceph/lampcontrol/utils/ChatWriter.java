package cz.ceph.lampcontrol.utils;

import org.bukkit.ChatColor;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class ChatWriter {

    public static String prefix(String string) {
        return ChatColor.translateAlternateColorCodes('&', getMain().getMainConfig().getPluginPrefix() + " " + ChatColor.WHITE + string);
    }

    public static String noPrefix(String string) {
        return ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + string);
    }

    public static String noPermissions() {
        return ChatColor.translateAlternateColorCodes('&', "You &cdon't &fhave enough &7permissions &fto do this.");
    }
}
