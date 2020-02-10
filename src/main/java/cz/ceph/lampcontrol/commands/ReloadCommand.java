package cz.ceph.lampcontrol.commands;

import com.google.common.collect.ImmutableSet;
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
public class ReloadCommand implements BukkitCommand {
    public ReloadCommand(Main main) {
    }

    @Override
    public boolean onPlayerCommand(Player player, ICommand iCommand, List<String> list) {
        Main.getInstance().onDisable();
        Main.getInstance().onEnable();
        return true;
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender consoleCommandSender, ICommand iCommand, List<String> list) {
        Main.getInstance().onDisable();
        Main.getInstance().onEnable();
        return true;
    }

    @Override
    public Iterable<String> onPlayerTabComplete(Player player, ICommand iCommand, List<String> list) {
        return ImmutableSet.of();
    }

    @Override
    public Iterable<String> onConsoleTabComplete(ConsoleCommandSender consoleCommandSender, ICommand iCommand, List<String> list) {
        return ImmutableSet.of();
    }

    @Override
    public @NotNull String getCommandName() {
        return "lampcontrol";
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "reload";
    }

    @Override
    public @NotNull String getPermission() {
        return "lampcontrol.command.reload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Reloads the pugin";
    }

    @Override
    public @NotNull String getUsage() {
        return "What, just /lampcontrol reload";
    }
}
