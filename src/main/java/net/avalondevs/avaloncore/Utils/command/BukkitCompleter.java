package net.avalondevs.avaloncore.Utils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * Command Framework - BukkitCompleter <br>
 * An implementation of the TabCompleter class allowing for multiple tab
 * completers per command
 */
public class BukkitCompleter implements TabCompleter {

    private final Map<String, Entry<Method, Object>> completers = new HashMap<String, Entry<Method, Object>>();
    public Map<String, List<String>> simpleCompleteors = new HashMap<>();

    public void addCompleter(String label, Method m, Object obj) {
        completers.put(label, new AbstractMap.SimpleEntry<>(m, obj));
    }

    public void addCompleter(String label, List<String> strings) {
        simpleCompleteors.put(label, strings);
    }

    @SuppressWarnings("unchecked")
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                if (!args[x].equals("") && !args[x].equals(" ")) {
                    buffer.append(".").append(args[x].toLowerCase());
                }
            }
            String cmdLabel = buffer.toString();
            if (completers.containsKey(cmdLabel)) {
                Entry<Method, Object> entry = completers.get(cmdLabel);
                try {
                    List<String> startsWith = new ArrayList<>();
                    List<String> completions = (List<String>) entry.getKey().invoke(entry.getValue(),
                            new CommandAdapter(sender, command, label, args, cmdLabel.split("\\.").length - 1, null));
                    String starter = args[args.length - 1];
                    for (String completion : completions) {
                        if (completion.startsWith(starter))
                            startsWith.add(completion);
                    }
                    return startsWith;
                } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (simpleCompleteors.containsKey(cmdLabel)) {

                List<String> startsWith = new ArrayList<>();
                List<String> completions = simpleCompleteors.get(cmdLabel);
                String starter = args[args.length - 1];
                for (String completion : completions) {
                    if (completion.startsWith(starter))
                        startsWith.add(completion);
                }
                return startsWith;

            }

        }
        return null;
    }

}