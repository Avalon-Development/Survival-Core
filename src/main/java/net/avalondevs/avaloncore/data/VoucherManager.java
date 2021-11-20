package net.avalondevs.avaloncore.data;

import lombok.Getter;
import net.avalondevs.avaloncore.Utils.Color;
import net.avalondevs.avaloncore.Utils.LuckPermsAdapter;
import net.luckperms.api.model.group.Group;

import java.util.HashMap;
import java.util.Map;

import static net.avalondevs.avaloncore.data.Voucher.TEMPLATE_NAME;

public class VoucherManager {

    @Getter
    public static VoucherManager instance = new VoucherManager();

    public Map<String, Voucher> cache = new HashMap<>();

    public Voucher get(Group group) {

        Voucher voucher = new Voucher(group);

        if (!cache.containsValue(voucher))
            cache.put(voucher.buildDisplayName(), voucher);

        return voucher;

    }

    public Voucher getWithName(String name) {

        return cache.get(name);

    }

    public void cache() {

        LuckPermsAdapter.luckperms.getGroupManager().loadAllGroups();

        LuckPermsAdapter.luckperms.getGroupManager().getLoadedGroups().forEach(group -> {

            String name = group.getDisplayName();
            if (name == null)
                name = group.getFriendlyName();

            cache.put(Color.fmt(TEMPLATE_NAME.replace("%rank%", name)), new Voucher(group));

        });

    }
}
