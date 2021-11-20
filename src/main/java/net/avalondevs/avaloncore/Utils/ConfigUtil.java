package net.avalondevs.avaloncore.Utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigUtil {

    /**
     * compares 2 configs with each other
     *
     * @param config1
     * @param config2
     * @return a set of all the non-existent keys in {@code config2}
     */
    public Set<String> compare(Configuration config1, Configuration config2) {

        Set<String> diff = new HashSet<String>();

        Set<String> keysConfig1 = config1.getKeys(true);
        Set<String> keysConfig2 = config2.getKeys(true);

        diff = keysConfig1.stream().filter(f -> !keysConfig2.contains(f)).collect(Collectors.toSet());

        return diff;

    }

    public void updateConfig(Configuration newConfig, Configuration oldConfig) {

        Set<String> diff = compare(newConfig, oldConfig);

        Bukkit.getLogger().info("Starting YAML update on " + oldConfig.getName());
        Bukkit.getLogger().info("Found " + diff.size() + " keys to update");

        if (diff.size() > 0) {

            for (String string : compare(newConfig, oldConfig)) {

                Bukkit.getLogger().info(string);

                oldConfig.set(string, newConfig.get(string));

            }

        }

    }

    @SneakyThrows
    public void updateConfig(Plugin plugin, String resource) {

        plugin.saveResource(resource, false); // ensure exists

        File file = new File(plugin.getDataFolder(), resource);

        FileConfiguration config = new YamlConfiguration();
        config.load(file);

        FileConfiguration newConfig = new YamlConfiguration();

        newConfig.load(new InputStreamReader(plugin.getResource(resource)));

        updateConfig(newConfig, config);

        config.save(file);

    }

    public Configuration ensureConfig(File configFile) {

        return YamlConfiguration.loadConfiguration(configFile);

    }

    @SneakyThrows
    public void saveConfig(Configuration configuration, File file) {

        ((FileConfiguration) configuration).save(file);

    }

}
