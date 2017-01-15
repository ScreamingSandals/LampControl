package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampreload", alias = "reloadlamp")
public class ReloadCommand implements IBasicCommand {

    @Override
    public String getPermission() {
        return "lampcontrol.command.on";
    }

    @Override
    public String getDescription() {
        return getMain().getLocalizations().get(getMain().language, "command.on_lamp_description");
    }

    @Override
    public String getUsage() {
        return "/onlamp or /lampon";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        getMain().getMainConfig().initializeConfig();
        sender.sendMessage(getMain().getLocalizations().get(getMain().language,"info.config_reloaded"));

        return true;
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        getMain().getMainConfig().initializeConfig();
        player.sendMessage(getMain().getLocalizations().get(getMain().language,"info.config_reloaded"));

        return false;
    }
}
