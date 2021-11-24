package net.avalondevs.avaloncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class GuiListener implements Listener {
    @EventHandler
    private void GuiInteract(InventoryInteractEvent e) {
        Player player = (Player) e.getWhoClicked();
        player.sendMessage("Testing123");


        if (e.getView().getTitle().contains("Tags")) {

            e.setCancelled(true);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta clear suffixes");
            if (player.getItemOnCursor().getItemMeta().getDisplayName().contains("Avalon")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add avalon");
            } else if (player.getItemOnCursor().getItemMeta().getDisplayName().contains("Minecrafter")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add minecrafter");

            } else if (player.getItemOnCursor().getItemMeta().getDisplayName().contains("Lover")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add lover");

            } else if (player.getItemOnCursor().getItemMeta().getDisplayName().contains("Friend")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add friend");

            }

        }
    }
}
