package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import cz.ceph.lampcontrol.utils.ChatWriter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampcontrol", alias = {"lc", "lamp"})
public class BasicCommand implements IBasicCommand {

    @Override
    public String getPermission() {
        return "lampcontrol.command.basic";
    }

    @Override
    public String getDescription() {
        return "Basic command for LampControl plugin.";
    }

    @Override
    public String getUsage() {
        return "/lampcontrol";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(ChatWriter.noPrefix("&eLamp&cControl &fversion &e" + LampControl.pluginInfo + " &fby &eCeph"));
        sender.sendMessage(getMain().getLocalizations().get(getMain().language, "info.lampcontrol_use_help"));
        return true;
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        player.sendMessage(ChatWriter.noPrefix("&eLamp&cControl &fversion &e" + LampControl.pluginInfo + " &fby &eCeph"));
        player.sendMessage(getMain().getLocalizations().get(getMain().language, "info.lampcontrol_use_help"));
        return true;
    }

}
