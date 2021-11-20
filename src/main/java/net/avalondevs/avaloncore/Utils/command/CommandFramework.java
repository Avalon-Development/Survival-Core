package net.avalondevs.avaloncore.Utils.command;

import net.avalondevs.avaloncore.Utils.I18N;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * Command Framework - CommandFramework <br>
 * The main command framework class used for controlling the framework.
 */
public class CommandFramework implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<String, Entry<Method, Object>>();
    private final JavaPlugin plugin;
    private CommandMap map;

    /**
     * Initializes the command framework and sets up the command maps
     */
    public CommandFramework(JavaPlugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    /**
     * Handles commands. Used in the onCommand method in your JavaPlugin class
     *
     * @param sender The {@link CommandSender} parsed from
     *               onCommand
     * @param cmd    The {@link org.bukkit.command.Command} parsed from onCommand
     * @param label  The label parsed from onCommand
     * @param args   The arguments parsed from onCommand
     * @return Always returns true for simplicity's sake in onCommand
     */
    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);
                if (!command.permission().equalsIgnoreCase("") && !sender.hasPermission(command.permission())) {
                    sender.sendMessage("You don't have permission to execute this command");
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage("This command can only be executed as a player");
                    return true;
                }

                CommandAdapter adapter = new CommandAdapter(sender, cmd, label, args,
                        cmdLabel.split("\\.").length - 1, command);
                try {
                    method.invoke(methodObject, adapter);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return !adapter.fail;
            }
        }
        defaultCommand(new CommandAdapter(sender, cmd, label, args, 0, null));
        return true;
    }

    /**
     * Registers all command and completer methods inside of the object. Similar
     * to Bukkit's registerEvents method.
     *
     * @param obj The object to register the commands of
     */
    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandAdapter.class) {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(command, command.name(), m, obj);
            } else if (m.getAnnotation(Completer.class) != null) {
                Completer comp = m.getAnnotation(Completer.class);
                if (m.getParameterTypes().length != 1
                        || m.getParameterTypes()[0] != CommandAdapter.class) {
                    System.out.println("Unable to register tab completer " + m.getName()
                            + ". Unexpected method arguments");
                    continue;
                }
                if (m.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                    continue;
                }
                registerCompleter(comp.name(), m, obj);
                for (String alias : comp.aliases()) {
                    registerCompleter(alias, m, obj);
                }
            }
        }
    }

    /**
     * Registers all the commands under the plugin's help
     */
    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                org.bukkit.command.Command cmd = map.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help,
                "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

        String cmdLabel = label.split("\\.")[0].toLowerCase();


        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);

            cmd.setAliases(Arrays.asList(command.aliases()));

            map.register(plugin.getName(), cmd);
        }

        BukkitCommand cmd = (BukkitCommand) map.getCommand(cmdLabel);

        if (plugin.getServer().getPluginCommand(cmdLabel) != null) { // global overwrite

            plugin.getServer().getPluginCommand(cmdLabel).setExecutor(this);

        }

        String description = command.description();
        if (command.i18n() && description.isEmpty()) {
            description = I18N.getInstance().format("command." + label + ".description");
        }

        if (!description.isEmpty())
            cmd.setDescription(description);

        String help = command.usage();
        if (command.i18n() && help.isEmpty()) {

            help = I18N.getInstance().format("command." + label + ".help");

        }

        if (!help.isEmpty())
            cmd.setUsage(help);

        if (command.completions().length != 0) {
            if (cmd.completer == null) {
                cmd.completer = new BukkitCompleter();
            }
            cmd.completer.simpleCompleteors.put(cmdLabel, Arrays.asList(command.completions()));
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }
        if (map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand) map.getCommand(cmdLabel);
            if (command.completer == null) {
                command.completer = new BukkitCompleter();
            }
            command.completer.addCompleter(label, m, obj);
        } else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                if (field.get(command) == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    BukkitCompleter completer = (BukkitCompleter) field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName()
                            + ". A tab completer is already registered for that command!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(CommandAdapter args) {
        args.getSender().sendMessage(args.getLabel() + " is not handled! Oh noes!");
    }

}
