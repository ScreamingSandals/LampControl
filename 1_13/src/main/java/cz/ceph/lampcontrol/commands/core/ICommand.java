package cz.ceph.lampcontrol.commands.core;

/**
 * Created by SiOnzee on 10.01.2017.
 */

public interface ICommand {

    /**
     * Requested permission for command execution
     */
    String getPermission();

    /**
     * Description what command do
     */
    String getDescription();

    /**
     * Correct usage of command
     * */
    String getUsage();
}
