package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampreload", alias = "reloadlamp")
public class ReloadCommand implements IBasicCommand {

    private String language = getMain().language;

    @Override
    public String getPermission() {
        return "lampcontrol.command.reload";
    }

    @Override
    public String getDescription() {
        return getMain().getLocalizations().get(language, "command.reload_description");
    }

    @Override
    public String getUsage() {
        return "/lampreload or /reloadlamp";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        getMain().getMainConfig().initializeConfig();
        sender.sendMessage(ChatWriter.prefix(getMain().getLocalizations().get(language,"info.config_reloaded")));

        return true;
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        getMain().getMainConfig().initializeConfig();
        player.sendMessage(ChatWriter.prefix(getMain().getLocalizations().get(language,"info.config_reloaded")));

        return true;
    }
}
