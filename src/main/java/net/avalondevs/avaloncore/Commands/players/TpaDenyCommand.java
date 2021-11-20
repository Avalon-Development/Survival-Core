package net.avalondevs.avaloncore.Commands.players;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpaDenyCommand {

    @Command(name = "tpadeny", permission = "")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();
        if (TpaCommand.req.containsValue(player.getUniqueId())) {
            for (Map.Entry<UUID, UUID> entry : TpaCommand.req.entrySet()) {
                if (entry.getValue().equals(player.getUniqueId())) {
                    TpaCommand.req.remove(entry.getKey());
                    Player originalSender = Bukkit.getPlayer(entry.getKey());
                    originalSender.sendMessage(ChatColor.GOLD + "Your TPA request was denied!");
                    player.sendMessage(ChatColor.GOLD + "Denied TPA request.");
                    break;
                }
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "You don't have any pending requests!");
        }
    }
}
