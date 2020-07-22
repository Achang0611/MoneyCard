package com.vm.plugin;

import com.vm.plugin.Command.MenuCommand;
import com.vm.plugin.Config.ConfigVersionChecker;
import com.vm.plugin.Config.Configuration;
import com.vm.plugin.Event.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyCardPlugin extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    private Configuration menu, chat;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        menu = new Configuration(this, "Menu");
        chat = new Configuration(this, "Chat");

        if (!new ConfigVersionChecker(this).check()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new MenuCommand(this);
        new ClickMenu(this);
        new ClickCard(this);
        new SetPlayer(this);
        new SetMoney(this);
        new SetAmount(this);
        new AntiCrafting(this);
    }

    @Override
    public void onDisable() {
        reloadAll();
    }

    public FileConfiguration getMenu() {
        return menu.getConfig();
    }

    public FileConfiguration getChat() {
        return chat.getConfig();
    }

    public void reloadMenu() {
        menu.reload();
    }

    public void reloadChat() {
        chat.reload();
    }

    public void reloadAll() {
        reloadMenu();
        reloadChat();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Matcher CardMatcher(String localName) {
        return Pattern.compile("MoneyCard\\{amount: (\\d+)}").matcher(localName);
    }

    public static Economy getEcon() {
        return econ;
    }
}
