package cz.ceph.lampcontrol.commands.core;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by SiOnzee on 10.01.2017.
 */

public interface IBasicCommand {

    /**
     * Invoke when command execute player
     */
    boolean onPlayerCommand(Player player, String[] args);

    /**
     * Invoke when command execute console
     */
    boolean onConsoleCommand(ConsoleCommandSender sender, String[] args);

}
