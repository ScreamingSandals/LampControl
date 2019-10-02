package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import misat11.lib.lang.I18n;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static misat11.lib.lang.I18n.i18n;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampconfig", alias = {"lampc"})
public class ControlCommand implements IBasicCommand {

    @Override
    public String getPermission() {
        return "lampcontrol.command.cionfig";
    }

    @Override
    public String getDescription() {
        return i18n("command.on_lamp_description");
    }

    @Override
    public String getUsage() {
        return "/lampconfig [config value] [change to value] or alias lampc.";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(i18n("error.console_use"));
        return true;


        // needs to be done
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        return true;

        // still wip shit
    }
}
