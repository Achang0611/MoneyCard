package com.vm.plugin.Config;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;

public class ConfigVersionChecker {

    private final MoneyCardPlugin plugin;

    public ConfigVersionChecker(MoneyCardPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean check() {
        String menuVersion = YamlConfiguration
                .loadConfiguration(new InputStreamReader(plugin.getResource("Menu.yml")))
                .getString("version");
        String chatVersion = YamlConfiguration
                .loadConfiguration(new InputStreamReader(plugin.getResource("Chat.yml")))
                .getString("version");

        boolean menuVersionCheck = menuVersion.equals(plugin.getMenu().getString("version"));
        boolean chatVersionCheck = chatVersion.equals(plugin.getChat().getString("version"));

        if (menuVersionCheck && chatVersionCheck) {
            plugin.getLogger().info("MoneyCard Config版本同步!");
        } else {
            plugin.getLogger().warning("MoneyCard Config版本不同步!");
            plugin.getLogger().warning("請刪除plugins\\MoneyCard資料夾，並重新啟動伺服器!");
        }

        return menuVersionCheck && chatVersionCheck;
    }
}
