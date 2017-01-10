package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ceph on 10.01.2017.
 */

@RegisterCommand(value = "lampcontrol", alias = "lc")
public class BasicCommand implements IBasicCommand {
    private LampControl plugin;

    private BasicCommand(LampControl plugin){
        this.plugin = plugin;
    }

    public String getPermission() {
        return "lampcontrol.command.basic";
    }

    public String getDescription() {
        return "Basic command for LampControl plugin.";
    }

    public String getUsage() {
        return "/lampcontrol";
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(plugin.getLocalizations().get(plugin.language, "info.lampcontrol_info"));
        sender.sendMessage(plugin.getLocalizations().get(plugin.language, "info.lampcontrol_use_help"));
        return true;
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        player.sendMessage(plugin.getLocalizations().get(plugin.language, "info.lampcontrol_info"));
        player.sendMessage(plugin.getLocalizations().get(plugin.language, "info.lampcontrol_use_help"));
        return true;
    }

}
