package cz.ceph.lampcontrol.commands;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.commands.core.IBasicCommand;
import cz.ceph.lampcontrol.commands.core.RegisterCommand;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamceph on 05.10.2018.
 */

@RegisterCommand(value = "lcgetmaterials")
public class ListMaterialsDev implements IBasicCommand {

    @Override
    public String getPermission() {
        return "lampcontrol.command.lcgetmaterials";
    }

    @Override
    public String getDescription() {
        return "Developement tool to get materials in config list";
    }

    @Override
    public String getUsage() {
        return "/lcgetmaterials";
    }


    @Override
    public boolean onPlayerCommand(Player player, String[] args) {

        List<Material> materials = new ArrayList<>();
        return false;
    }

    @Override
    public boolean onConsoleCommand(ConsoleCommandSender sender, String[] args) {
        return false;
    }
}
