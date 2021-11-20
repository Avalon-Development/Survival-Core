package net.avalondevs.avaloncore.Commands.Admin;

import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SetspawnCommand {

    @Command(name = "setspawn", permission = "core.admin.setspawn")
    public void onCommand(CommandAdapter adapter) {
        Player player = adapter.getPlayer();
        World world = player.getWorld();

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        world.setSpawnLocation(x, y, z);
        adapter.sendMessage("&7Set spawn point to &e" + x + "&e&n" + y + "&E&n" + z);
    }
}
