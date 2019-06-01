package cz.ceph.lampcontrol.commands.core;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SiOnzee on 10.01.2017.
 */

public interface IRegexCommand extends ICommand {

    Pattern getCommandPattern();

    /**
     * Invoke when command execute player
     */
    boolean onPlayerCommand(Player player, Matcher args);

    /**
     * Invoke when command execute console
     */
    boolean onConsoleCommand(ConsoleCommandSender sender, Matcher args);

}
