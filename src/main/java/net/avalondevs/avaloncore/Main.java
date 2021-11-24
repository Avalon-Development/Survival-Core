package net.avalondevs.avaloncore;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.avalondevs.avaloncore.Utils.*;
import net.avalondevs.avaloncore.Utils.command.CommandFramework;
import net.avalondevs.avaloncore.data.Voucher;
import net.avalondevs.avaloncore.data.VoucherManager;
import net.avalondevs.avaloncore.listeners.GuiListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main plugin;

    @Getter
    @Setter
    public static Main instance;

    CommandFramework framework = new CommandFramework(this); // initialize a new framework
    public static Main getPlugin() {
        return plugin;
    }
    private Economy econ;

    @SneakyThrows
    public void onEnable() {

        setInstance(this);
        plugin = this;

        Logger.init();

        Bukkit.getConsoleSender().sendMessage("===============");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "* Name: &f" + getDescription().getName());
        Bukkit.getConsoleSender().sendMessage("===============");

        Logger.info(ChatColor.AQUA + "Loading libraries ...");

        LuckPermsAdapter.init();

        saveDefaultConfig();


        I18N i18n = new I18N();

        if (!setupEconomy()) {
            getLogger().severe("VAULT IS NOT ENABLED -- ADD IT OR BREAKAGE MAY OCCUR");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        CommandRegistry.registerModuleCommands(framework);
        CommandRegistry.registerCommands(framework);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        Voucher.cache();
        VoucherManager.instance.cache();
    }

    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage("===============");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Plugin disabled");
        Bukkit.getConsoleSender().sendMessage("===============");
    }

    public boolean setupEconomy () {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return true;
    }

}
