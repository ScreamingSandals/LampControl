package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.CommandHandler;
import cz.ceph.lampcontrol.commands.core.ICommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ceph on 10.01.2017.
 */

public class HelpCommand {
    private LampControl plugin;
    private CommandHandler commandHandler;

    private HelpCommand(LampControl plugin) {
        this.plugin = plugin;
        commandHandler = new CommandHandler(plugin);
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(ChatWriter.noPrefix("&e------ &eLamp&cControl &fHelp Menu &e------"));

        for (ICommand command : commandHandler.getAvailableCommands().values()) {
            sender.sendMessage(ChatWriter.noPrefix(command.getUsage() + " &8---&f " + command.getDescription()));
        }

        return true;
    }

    public String getPermission() {
        return "lampcontrol.command.help";
    }

    public String getDescription() {
        return "Main help command for LampControl.";
    }

    public String getUsage() {
        return "/lampcontrol help";
    }

    public boolean onPlayerCommand(Player player, String[] args) {
        return onCommand(player, args);
    }

    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        return onCommand(sender, args);
    }
}
