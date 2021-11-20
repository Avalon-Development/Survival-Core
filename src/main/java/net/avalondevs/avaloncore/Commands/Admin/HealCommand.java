package net.avalondevs.avaloncore.Commands.Admin;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.entity.Player;

public class HealCommand {

    @Command(name = "heal", permission = "core.admin.heal")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();

        player.setHealth(20);
        adapter.sendMessage("&eYou &7have been healed");
    }
}
