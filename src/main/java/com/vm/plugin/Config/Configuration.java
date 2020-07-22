package com.vm.plugin.Config;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private File file;
    private FileConfiguration cfg;
    private final MoneyCardPlugin plugin;

    public Configuration(MoneyCardPlugin plugin, String fileName) {
        this.plugin = plugin;
        setup(fileName);
    }

    private void setup(String fileName) {
        File dataFolder = plugin.getDataFolder();
        if (!fileName.contains(".yml")) {
            fileName += ".yml";
        }
        file = new File(dataFolder, fileName);
        plugin.saveResource(fileName, false);

        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return cfg;
    }

    public void reload() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }
}
