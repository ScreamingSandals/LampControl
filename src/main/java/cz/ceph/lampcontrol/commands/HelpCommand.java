package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.ICommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampcontrol", alias = "lc")
public class HelpCommand implements IBasicCommand {

    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(" ");
        sender.sendMessage(ChatWriter.noPrefix("&e------ &eLamp&cControl &fHelp Menu &e------"));

        for (ICommand command : getMain().getCommandHandler().getAvailableCommands().values()) {
            sender.sendMessage(ChatWriter.noPrefix(command.getUsage() + " &8---&f " + command.getDescription()));
        }
        sender.sendMessage(" ");

        return true;
    }

    @Override
    public String getPermission() {
        return "lampcontrol.command.help";
    }

    @Override
    public String getDescription() {
        return getMain().getLocalizations().get(getMain().language, "command.help_description");
    }

    @Override
    public String getUsage() {
        return "/lampcontrol or /lc";
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        return onCommand(player, args);
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        return onCommand(sender, args);
    }
}
