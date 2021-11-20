package net.avalondevs.avaloncore.Commands.voucher;

import net.avalondevs.avaloncore.Utils.LuckPermsAdapter;
import net.avalondevs.avaloncore.Utils.Utils;
import net.avalondevs.avaloncore.Utils.command.Command;
import net.avalondevs.avaloncore.Utils.command.CommandAdapter;
import net.avalondevs.avaloncore.data.Voucher;
import net.avalondevs.avaloncore.data.VoucherManager;
import net.luckperms.api.model.group.Group;
import org.bukkit.inventory.ItemStack;

public class VoucherCommand {

    @Command(name = "voucher", permission = "core.voucher", completions = {"info", "add", "get"})
    public void onRootCommand(CommandAdapter adapter) {

        adapter.sendMessage("");


    }

    // TODO make error messages

    @Command(name = "voucher.info", permission = "core.voucher.info")
    public void onInfoCommand(CommandAdapter adapter) {

        adapter.requireArg(0, (name) -> {

            Group group = LuckPermsAdapter.getByName(name);
            if (group == null) {

                adapter.sendMessage("error.rank.not-found", name);
                return;

            }


            Voucher voucher = VoucherManager.getInstance().get(group);

            adapter.sendMessage(Utils.PREFIX + " Info for voucher:");
            adapter.sendMessage("Rank: " + LuckPermsAdapter.getDefiniteName(voucher.group()));

        });

    }

    @Command(name = "voucher.get", permission = "core.voucher.get")
    public void onGetCommand(CommandAdapter adapter) {

        adapter.requireArg(0, (rank) -> {

            Group group = LuckPermsAdapter.getByName(rank);
            if (group == null) {

                adapter.sendMessage("error.rank.not-found", rank);
                return;

            }

            Voucher voucher = VoucherManager.getInstance().get(group);

            ItemStack stack = voucher.create();

            adapter.getPlayer().getInventory().addItem(stack);


        });

    }

}
