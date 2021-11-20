package net.avalondevs.avaloncore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

    public static String PREFIX = Color.fmt("#81dbff&lA#61c7fb&lV#46b2f7&lA#389df0&lL#3e86e6&lO#4f6dd8&lN");

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void createMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, chat("&7Tags Menu"));

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta book_meta = book.getItemMeta();

        book_meta.setDisplayName("test");

        gui.setItem(13, book);
        player.openInventory(gui);
    }
}
