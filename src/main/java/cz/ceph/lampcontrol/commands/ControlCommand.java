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

@RegisterCommand(value = "lampcontrol", alias = {"controllamp", "lc"})
public class ControlCommand implements IBasicCommand {

    private String language = getMain().language;

    @Override
    public String getPermission() {
        return "lampcontrol.command.reload";
    }

    @Override
    public String getDescription() {
        return getMain().getLocalizations().get(language, "command.on_lamp_description");
    }

    @Override
    public String getUsage() {
        return "/lampreload or /reloadlamp";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(ChatWriter.prefix(getMain().getLocalizations().get(language, "error.console_use")));
        return true;
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        return true;
    }
}
