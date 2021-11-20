package net.avalondevs.avaloncore.Commands.players;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

import static net.avalondevs.avaloncore.Main.plugin;

public class TpaCommand {

    static HashMap<UUID, UUID> req = new HashMap<>();

    @Command(name = "tpa")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();
        if (adapter.length() < 1) {
            adapter.fail();
        }
        if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(adapter.getArgs(0)))) {
            player.sendMessage(ChatColor.RED + "Player is not online!");
        }
        Player target = Bukkit.getPlayer(adapter.getArgs(0));

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You may not teleport to yourself!");
        }
        if (req.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.GOLD + "You already have a pending request!");
        }
        target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GOLD + " wants to teleport to you. \nType " + ChatColor.RED + "/tpaccept" + ChatColor.GOLD + " to accept this request.\nType " + ChatColor.RED + "/tpdeny" + ChatColor.GOLD + " to deny this request.\nYou have 5 minutes to respond.");
        req.put(player.getUniqueId(), target.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Send TPA request to " + ChatColor.RED + target.getName());
        (new BukkitRunnable() {
            public void run() {
                req.remove(player.getUniqueId());
            }
        }).runTaskLaterAsynchronously(plugin, 6000L);
    }
}
