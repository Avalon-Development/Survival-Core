package net.avalondevs.avaloncore.data;

import net.avalondevs.avaloncore.Main;
import net.avalondevs.avaloncore.Utils.Color;
import net.avalondevs.avaloncore.Utils.DataParser;
import net.luckperms.api.model.group.Group;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public record Voucher(Group group) {

    public static ItemStack VOUCHER_TEMPLATE;
    public static List<String> TEMPLATE_LORE;
    public static String TEMPLATE_NAME;

    public static void cache() {

        VOUCHER_TEMPLATE = DataParser.readItem(Main.getInstance().getConfig().getString("vouchers.default.material"));
        TEMPLATE_LORE = Main.getInstance().getConfig().getStringList("vouchers.default.lore");

        TEMPLATE_LORE = TEMPLATE_LORE.stream().map(Color::fmt).collect(Collectors.toList());

        TEMPLATE_NAME = Main.getInstance().getConfig().getString("vouchers.default.name");

    }

    public String buildDisplayName() {

        String name = group.getDisplayName();
        if (name == null)
            name = group.getFriendlyName();

        return Color.fmt(TEMPLATE_NAME.replace("%rank%", name));

    }

    public ItemStack create() {

        ItemMeta metadata = VOUCHER_TEMPLATE.getItemMeta();

        String name = group.getDisplayName();
        if (name == null)
            name = group.getFriendlyName();

        String finalName = name;
        metadata.setLore(TEMPLATE_LORE.stream().map(s -> s.replace("%rank%", Color.fmt(finalName))).collect(Collectors.toList()));

        String itemName = Color.fmt(TEMPLATE_NAME.replace("%rank%", finalName));

        metadata.setDisplayName(itemName);

        ItemStack newItem = VOUCHER_TEMPLATE.clone();

        newItem.setItemMeta(metadata);

        return newItem;

    }
}
