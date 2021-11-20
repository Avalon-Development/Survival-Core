package net.avalondevs.avaloncore.Commands.players;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static net.avalondevs.avaloncore.Commands.players.TpaCommand.req;

public class TpAccept {


    @Command(name = "tpaccept", permission = "")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();
        player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
        if (req.containsValue(player.getUniqueId())) {
            player.sendMessage(ChatColor.GOLD + "TPA request accepted!");
            for (Map.Entry<UUID, UUID> entry : req.entrySet()) {
                if (entry.getValue().equals(player.getUniqueId())) {
                    Player tpRequester = Bukkit.getPlayer(entry.getKey());
                    tpRequester.teleport(player);
                    req.remove(entry.getKey());
                    break;
                }
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "You don't have any pending requests!");
        }
    }
}
