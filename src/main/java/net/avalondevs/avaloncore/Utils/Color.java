package net.avalondevs.avaloncore.Utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Color {

    private final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

    public String fmt(String input) {

        if (Bukkit.getVersion().contains("1.16")) {

            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {

                String color = input.substring(matcher.start(), matcher.end());
                input = input.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(input);
            }

        }

        return ChatColor.translateAlternateColorCodes('&', input);

    }

}
