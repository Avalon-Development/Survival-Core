package net.avalondevs.avaloncore.Commands.Admin;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EatCommand {

    @Command(name = "eat", aliases = "feed", permission = "core.admin.eat")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();

        adapter.optionalArg(0, (name) -> {

            Player target = Bukkit.getPlayer(name);

            if(target == null) {

                adapter.sendMessage("error.player.not-found", name);

            }else {

                target.setSaturation(20);
                target.setFoodLevel(20);
                adapter.sendMessage("command.eat.execute", target.getName());

            }

        });

        player.setSaturation(20);
        player.setFoodLevel(20);
        adapter.sendMessage("command.eat.execute", player.getName());

    }
}
