package net.avalondevs.avaloncore.Commands.players;

import net.avalondevs.avaloncore.Utils.Utils;
import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.entity.Player;

public class TagCommand {
    @Command(name = "tags", permission = "")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();

        Utils.createMenu(player, "tags");

    }
}
