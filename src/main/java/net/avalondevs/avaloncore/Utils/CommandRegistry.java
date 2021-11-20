package net.avalondevs.avaloncore.Utils;

import lombok.experimental.UtilityClass;
import net.avalondevs.avaloncore.Commands.Admin.EatCommand;
import net.avalondevs.avaloncore.Commands.Admin.HealCommand;
import net.avalondevs.avaloncore.Commands.Admin.SetspawnCommand;
import net.avalondevs.avaloncore.Commands.players.*;
import net.avalondevs.avaloncore.Commands.voucher.VoucherCommand;
import net.avalondevs.avaloncore.Utils.command.CommandFramework;

@UtilityClass
public class CommandRegistry {

    public void registerModuleCommands(CommandFramework framework) {

        framework.registerCommands(new VoucherCommand()); // load VoucherCommand into the framework

        framework.registerCommands(new SetspawnCommand());
        framework.registerCommands(new HealCommand());
        framework.registerCommands(new EatCommand());

        framework.registerCommands(new TpaCommand());
        framework.registerCommands(new TpAccept());
        framework.registerCommands(new TpaDenyCommand());
    }

    public void registerCommands(CommandFramework framework) {

    }

}
