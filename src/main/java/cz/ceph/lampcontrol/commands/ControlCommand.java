package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampcontrol", alias = {"controllamp", "lc"})
public class ControlCommand implements IBasicCommand {

    @Override
    public String getPermission() {
        return "lampcontrol.command.reload";
    }

    @Override
    public String getDescription() {
        return LampControl.localization.get("command.on_lamp_description");
    }

    @Override
    public String getUsage() {
        return "/lampreload or /reloadlamp";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(ChatWriter.prefix(LampControl.localization.get("error.console_use")));
        return true;


        // needs to be done
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        return true;

        // still wip shit
    }
}
