package net.avalondevs.avaloncore.Utils;

import lombok.experimental.UtilityClass;
import net.avalondevs.avaloncore.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class Logger {

    public final ChatColor ERROR = ChatColor.RED;
    public final ChatColor INFO = ChatColor.AQUA;
    public final ChatColor WARN = ChatColor.YELLOW;

    JavaPlugin instance;

    public void init() {

        instance = Main.getInstance();

    }

    public void error(String prefix, String message) {

        instance.getLogger().severe(ERROR + prefix + " " + message);

    }

    public void error(String message) {

        instance.getLogger().severe(ERROR + message);

    }

    public void info(String prefix, String message) {

        instance.getLogger().info(INFO + prefix + " " + message);

    }

    public void info(String message) {

        instance.getLogger().info(INFO + message);

    }

    public void warn(String prefix, String message) {

        instance.getLogger().warning(WARN + prefix + " " + message);

    }

    public void warn(String message) {

        instance.getLogger().warning(WARN + message);

    }


}
