package com.vm.plugin.Command;

import com.vm.plugin.Menu.MenuModel;
import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    private final MoneyCardPlugin plugin;

    public MenuCommand(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("MoneyCard").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getChat().getString("warning.ExecuteWithoutPlayer"));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("moneycard.cmd.player") && !p.hasPermission("moneycard.cmd.admin")) {
            p.sendMessage(plugin.getChat().getString("warning.NoPermission"));
            return true;
        }

        if (args.length == 0) {
            if (p.hasPermission("moneycard.cmd.admin")) {
                p.openInventory(new MenuModel(plugin).forAdmin());
            } else {
                p.openInventory(new MenuModel(plugin).forPlayer());
            }
        } else {
            if ("reload".equalsIgnoreCase(args[0])) {
                if (p.hasPermission("moneycard.cmd.reload")) {
                    plugin.reloadAll();
                    p.sendMessage(plugin.getChat().getString("general.Reloaded"));
                    return true;
                } else {
                    p.sendMessage(plugin.getChat().getString("warning.NoPermission"));
                }
            } else {
                p.sendMessage(plugin.getChat().getString("error.UnknownCommand"));
            }
        }
        return true;
    }
}
