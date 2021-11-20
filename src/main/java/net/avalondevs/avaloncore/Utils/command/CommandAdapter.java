package net.avalondevs.avaloncore.Utils.command;

import lombok.Getter;
import lombok.Setter;
import net.avalondevs.avaloncore.Utils.Color;
import net.avalondevs.avaloncore.Utils.I18N;
import net.avalondevs.avaloncore.Utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Command Framework - CommandArgs <br>
 * This class is passed to the command methods and contains various utilities as
 * well as the command info.
 */
public class CommandAdapter {

    private final CommandSender sender;
    private final org.bukkit.command.Command command;
    private final Command annotation;
    private final String label;
    private final String[] args;

    @Getter
    @Setter
    public int currentArg; // primarily used for Acceptors
    public boolean fail = false;

    protected CommandAdapter(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
                             int subCommand, Command annotation) {
        String[] modArgs = new String[args.length - subCommand];
        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(label);
        for (int x = 0; x < subCommand; x++) {
            buffer.append(".").append(args[x]);
        }
        String cmdLabel = buffer.toString();
        this.sender = sender;
        this.command = command;
        this.label = cmdLabel;
        this.args = modArgs;
        this.annotation = annotation;
    }

    /**
     * Gets the command sender
     *
     * @return
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Gets the original command object
     *
     * @return
     */
    public org.bukkit.command.Command getCommand() {
        return command;
    }

    /**
     * Gets the label including sub command labels of this command
     *
     * @return Something like 'test.subcommand'
     */
    public String getLabel() {
        return label.replace(".", " ");
    }

    /**
     * Gets all the arguments after the command's label. i.e. if the command
     * label was test.subcommand and the arguments were subcommand foo foo, it
     * would only return 'foo foo' because 'subcommand' is part of the command
     *
     * @return
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Gets the argument at the specified index
     *
     * @param index The index to get
     * @return The string at the specified index
     */
    public String getArgs(int index) {
        return args[index];
    }

    /**
     * Returns the length of the command arguments
     *
     * @return int length of args
     */
    public int length() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            return null;
        }
    }

    public void sendUsage() {
        fail = true;
    }

    /**
     * Same result as returning {@code null} in the
     * {@link org.bukkit.command.CommandExecutor#onCommand(CommandSender, org.bukkit.command.Command, String, String[])}
     * method
     */
    public void fail() {

        fail = true;

    }


    public void sendNoPermission() {

        sendMessage(Utils.PREFIX + " You don't have permission to execute this command");

    }

    public void sendMessage(String message) {

        getSender().sendMessage(Color.fmt(message));

    }

    public void sendMessage(String key, Object... args) {

        getSender().sendMessage(Utils.PREFIX + " " + Color.fmt(I18N.getInstance().format(key, args)));

    }

    public void sendFMessage(String key, Object... args) {

        getSender().sendMessage(Utils.PREFIX + " " + Color.fmt(I18N.getInstance().format(key, args)));

    }

    public void sendFMessageNoPrefix(String key, Object... args) {

        getSender().sendMessage(Color.fmt(I18N.getInstance().format(key, args)));

    }

    /**
     * a require args function
     * supposed to be used like this:
     * <br>
     * if(requireArg(index, (string) <br> {<br>
     * code when the required arg is present <br>
     * }))<br>
     * {<br>
     * code when the required arg is not present<br>
     * }<br>
     *
     * @param index
     * @param runner
     * @return
     */
    public boolean requireArg(int index, Consumer<String> runner) {

        if (index < length()) {
            if (runner != null)
                runner.accept(getArgs(index));
            return false;
        }

        return true;
    }

    public boolean requireArg(int index, Consumer<String> runner, Consumer<Void> error) {

        if (index < length()) {
            if (runner != null)
                runner.accept(getArgs(index));
            return false;
        }

        error.accept(null);
        return true;
    }

    public boolean optionalArg(int index, Consumer<String> runner) {

        return requireArg(index, runner);

    }

    public String range(int from, int to) {

        StringBuilder builder = new StringBuilder();

        for (int i = from; i < to; i++) {

            builder.append(args[i]);
            builder.append(" ");

        }

        return builder.substring(0, builder.length() - 1);

    }

    public String range(int from) {

        return range(from, args.length);

    }

}
