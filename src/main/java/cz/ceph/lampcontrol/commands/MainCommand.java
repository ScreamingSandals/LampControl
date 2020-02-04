package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.Main;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.screamingcommands.annotations.RegisterCommand;
import org.screamingsandals.lib.screamingcommands.api.BukkitCommand;
import org.screamingsandals.lib.screamingcommands.api.ICommand;

import java.util.List;

/**
 * @author ScreamingSandals team
 */
@RegisterCommand
public class MainCommand implements BukkitCommand {
    public MainCommand(Main main) {
    }

    @Override
    public boolean onPlayerCommand(Player player, ICommand iCommand, List<String> list) {
        return false;
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender consoleCommandSender, ICommand iCommand, List<String> list) {
        return false;
    }

    @Override
    public Iterable<String> onPlayerTabComplete(Player player, ICommand iCommand, List<String> list) {
        return null;
    }

    @Override
    public Iterable<String> onConsoleTabComplete(ConsoleCommandSender consoleCommandSender, ICommand iCommand, List<String> list) {
        return null;
    }

    @Override
    public @NotNull String getCommandName() {
        return "lampcontrol";
    }

    @Override
    public @NotNull String getPermission() {
        return "lampcontrol.command.main";
    }

    @Override
    public @NotNull String getDescription() {
        return "Main command of LampControl plugin";
    }

    @Override
    public @NotNull String getUsage() {
        return "What, just /lampcontrol";
    }

    @Override
    public @NotNull String[] getAliases() {
        return new String[]{"lc", "lamp"};
    }
}
