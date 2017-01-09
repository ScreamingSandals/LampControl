/*
	Code has been adapted from gomeow.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.lampcontrol.utils;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.ChatColor;

/**
 * Created by Ceph on 09.08.2016.
 */

public enum MessagesManager {
    PREFIX("plugin-prefix", "&8[&eLamp&cControl&8]&r"),
    TOO_MANY_ARGUMENTS("too-many-arguments", "&cToo many arguments!"),
    NO_WORLDEDIT("no-worldedit", "&cWorldEdit isn't installed. &aInstall &cit, if you need this feature."),
    NO_SELECTION("no-selection", "&cMake a region selection first."),
    NO_LAMPS_AFFECTED("no-lamps-affected", "&dNo lamps were affected."),
    ON_LAMPS("on-lamps", "&f%affected &dlamps were turned on."),
    OFF_LAMPS("off-lamps", "&f%affected &dlamps were turned off."),
    CONSOLE("console", "&cThis command cannot be run from the console.."),
    NO_PERMS("no-permissions", "&cYou don't have permissions to build here!");

    private String path;
    private String def;


    MessagesManager(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public String toString() {
        if (this == PREFIX)
            return ChatColor.translateAlternateColorCodes('&', LampControl.messagesConfig.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', LampControl.messagesConfig.getString(this.path, def));
    }
}
