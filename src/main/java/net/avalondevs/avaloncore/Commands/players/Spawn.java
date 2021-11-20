package net.avalondevs.avaloncore.Commands.players;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.entity.Player;

public class Spawn {

    @Command(name = "spawn", permission = "")
    public void onCommand(CommandAdapter adapter) {

        Player player = adapter.getPlayer();
        player.teleport(player.getWorld().getSpawnLocation());
    }
}
