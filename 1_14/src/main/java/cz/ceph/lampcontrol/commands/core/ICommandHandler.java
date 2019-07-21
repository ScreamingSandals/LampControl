package cz.ceph.lampcontrol.commands.core;

/**
 * Created by SiOnzee on 10.01.2017.
 */

public interface ICommandHandler {
    /**
     * Load all commands in whole .jar file
     *
     * @param mainClass Reference to class from .jar file
     */
    void loadCommands(Class<?> mainClass);

    /**
     * Register command manually
     */
    <Command extends ICommand> void registerCommand(Class<Command> command) throws CommandException;

    /**
     * Unregister all registerd commands
     */
    void unloadAll();
}
