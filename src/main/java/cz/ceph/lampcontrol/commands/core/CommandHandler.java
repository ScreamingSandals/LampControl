package cz.ceph.lampcontrol.commands.core;

import cz.ceph.lampcontrol.LampControl;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SiOnzee on 10.01.2017.
 * Modified by Ceph on 10.01.2017.
 */

public class CommandHandler implements CommandExecutor, ICommandHandler {

    private final LampControl plugin;
    private final HashMap<String, ICommand> availableCommands;
    private CommandMap commandMap = null;

    public CommandHandler(LampControl plugin) {
        this.plugin = plugin;
        availableCommands = new HashMap<>();

        SimplePluginManager simplePluginManager = (SimplePluginManager) Bukkit.getPluginManager();
        try {
            Field fieldCommandMap = simplePluginManager.getClass().getDeclaredField("commandMap");
            fieldCommandMap.setAccessible(true);
            commandMap = (CommandMap) fieldCommandMap.get(simplePluginManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            LampControl.debug.info("Registration of command failed.");
        }
    }

    @Override
    public void loadCommands(Class<?> mainClass) {
        try {
            JarFile jarFile = new JarFile(new File(mainClass.getProtectionDomain().getCodeSource().getLocation().toURI()));
            List<JarEntry> entries = Collections.list(jarFile.entries());
            entries.forEach(jarEntry -> {
                try {
                    if (!jarEntry.getName().endsWith(".class") || !jarEntry.getName().contains("cz/ceph"))
                        return;
                    Class<?> clazz = Class.forName(jarEntry.getName().replace("/", ".").replace(".class", ""));

                    if (!ICommand.class.isAssignableFrom(clazz)) {
                        return;
                    }

                    RegisterCommand annotation;
                    if ((annotation = clazz.getDeclaredAnnotation(RegisterCommand.class)) != null) {
                        registerCommand(annotation, createInstance(clazz));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private ICommand createInstance(Class<?> clazz) {
        Object clazzInstance = null;
        try {
            Constructor<?> constructorWithCore = null;
            try {
                constructorWithCore = clazz.getDeclaredConstructor(LampControl.class);
            } catch (NoSuchMethodException ignore) {
            }

            if (constructorWithCore == null) {
                clazzInstance = clazz.newInstance();
            } else {
                clazzInstance = constructorWithCore.newInstance(plugin);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (clazzInstance == null)
            LampControl.debug.info("[ERROR] Cannot create instance of " + clazz.getSimpleName() + " . Creating instance returned null.");
        return ICommand.class.cast(handle(clazzInstance));
    }

    @Override
    public <Command extends ICommand> void registerCommand(Class<Command> command) throws CommandException {
        RegisterCommand annotation = command.getDeclaredAnnotation(RegisterCommand.class);
        if (annotation == null) {
            throw new CommandException("Missing \"" + RegisterCommand.class.getSimpleName() + "\" annotation");
        }
        registerCommand(annotation, createInstance(command));
    }

    private Map<String, Command> getKnownCommands() {
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMap;
            Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            return (Map<String, Command>) field.get(simpleCommandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private boolean containsCommand(String command) {
        return getKnownCommands().containsKey(command);
    }

    private void removeCommand(String command) {
        getKnownCommands().remove(command);
    }

    @Override
    public void unloadAll() {
        Map<String, Command> knownCommands = getKnownCommands();
        availableCommands.keySet().forEach(knownCommands::remove);
    }


    private void registerCommand(RegisterCommand annotation, ICommand iCommand) {

        if (annotation == null) {
            LampControl.debug.info("[CommandHandler Error] Command annotation is null, cannot assign command name.");
            return;
        }

        if (iCommand == null) {
            LampControl.debug.info("[CommandHandler Error] Cannot register command" + annotation.value() + " due to nullable instance.");
            return;
        }

        if (commandMap == null) {
            LampControl.debug.info("[CommandHandler Error] CommandMap is null, can't add commands to CommandMap.");
            return;
        }

        String commandName = annotation.value();
        List<String> alias = Arrays.asList(annotation.alias());

        if (containsCommand(commandName))
            removeCommand(commandName);

        commandMap.register(commandName, new Command(commandName, iCommand.getDescription(), iCommand.getUsage(), alias) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                try {
                    return onCommand(commandSender, this, s, strings);
                } catch (Throwable tr) {
                    tr.printStackTrace();
                    return false;
                }
            }
        });

        availableCommands.put(commandName, iCommand);
        LampControl.debug.info("[CommandHandler] Registered command \"" + commandName + "\"");
    }

    public Map<String, ICommand> getAvailableCommands() {
        return Collections.unmodifiableMap(availableCommands);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();

        ICommand hozCommand = availableCommands.get(commandName);
        if (hozCommand == null) return false;

        if (hozCommand instanceof IRegexCommand) {
            IRegexCommand regexCommand = (IRegexCommand) hozCommand;
            String argsString = StringUtils.join(args, " ");

            Pattern pattern = regexCommand.getCommandPattern();
            Matcher matcher = pattern.matcher(argsString);
            Player player = (Player) commandSender;

            if (!matcher.find()) {
                commandSender.sendMessage("Invalid usage, use " + hozCommand.getUsage() + "instead.");
                return false;
            }

            if (commandSender instanceof ConsoleCommandSender)
                return regexCommand.onConsoleCommand((ConsoleCommandSender) commandSender, matcher);

            if (!commandSender.hasPermission(hozCommand.getPermission())) {
                commandSender.sendMessage(LampControl.localization.get("error.no_permissions"));
                return false;
            }
            return regexCommand.onPlayerCommand(player, matcher);
        } else if (hozCommand instanceof IBasicCommand) {
            IBasicCommand basicCommand = (IBasicCommand) hozCommand;

            if (commandSender instanceof ConsoleCommandSender)
                return basicCommand.onConsoleCommand((ConsoleCommandSender) commandSender, args);

            Player player = (Player) commandSender;

            if (!commandSender.hasPermission(hozCommand.getPermission())) {
                commandSender.sendMessage(LampControl.localization.get("error.no_permissions"));
                return false;
            }
            return basicCommand.onPlayerCommand(player, args);
        }

        return false;
    }

    private <T> T handle(T instance) {
        if (instance == null) return instance;
        Arrays.stream(instance.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            Object targetInstance = LampControl.class;

            try {
                field.set(instance, targetInstance);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        return instance;
    }
}
