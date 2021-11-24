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

    public static void createMenu(Player player, String type) {
        if (type == "tags") {
        Inventory gui = Bukkit.createInventory(null, 9, chat("&7Tags Menu"));

        ItemStack book1 = new ItemStack(Material.BOOK);
        ItemMeta book1_meta = book1.getItemMeta();

        ItemStack book2 = new ItemStack(Material.BOOK);
        ItemMeta book2_meta = book2.getItemMeta();

        ItemStack book3 = new ItemStack(Material.BOOK);
        ItemMeta book3_meta = book3.getItemMeta();

        ItemStack book4 = new ItemStack(Material.BOOK);
        ItemMeta book4_meta = book4.getItemMeta();

        book1_meta.setDisplayName(chat("&7[&bAvalon&7]&b"));
        book1.setItemMeta(book1_meta);

        book2_meta.setDisplayName(chat("&7[&aMinecrafter&7]&a"));
        book2.setItemMeta(book2_meta);

        book3_meta.setDisplayName(chat("&7[&5Lover&7]&5"));
        book3.setItemMeta(book3_meta);

        book4_meta.setDisplayName(chat("&7[&cFriend&7]&c"));
        book4.setItemMeta(book4_meta);

        gui.setItem(1, book1);
        gui.setItem(3, book2);
        gui.setItem(5, book3);
        gui.setItem(7, book4);

        player.openInventory(gui);
    } else {

        }
    }
}
