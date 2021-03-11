package com.vm.plugin.Event;

import com.vm.plugin.Chat.ChatProcessor;
import com.vm.plugin.Menu.ItemFactory;
import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.HashSet;

public class SetAmount implements Listener {

    private final MoneyCardPlugin plugin;
    private static HashSet<Player> player;

    public SetAmount(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        player = new HashSet<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (player.contains(p)) {
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(plugin.getChat().getString("general.Canceled"));
                cancel(p);
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(e.getMessage());
                if (amount < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException exception) {
                p.sendMessage(plugin.getChat().getString("warning.NotInt"));
                return;
            }
            player.remove(p);

            int money = SetMoney.getTarget().get(p);

            if (SetPlayer.getTarget().containsKey(p)) {
                Player target = SetPlayer.getTarget().get(p);
                if (target != null) {
                    target.getInventory().addItem(new ItemFactory(plugin).money(money, amount));
                    p.sendMessage(new ChatProcessor(plugin).giveMoney(p, amount, money));
                    target.sendMessage(new ChatProcessor(plugin).gotMoney(target, amount, money));
                } else {
                    Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
                    p.sendMessage(new ChatProcessor(plugin).giveAllMoney(players.size(), amount, money));
                    for (Player player :
                            players) {
                        player.getInventory().addItem(new ItemFactory(plugin).money(money, amount));
                        player.sendMessage(new ChatProcessor(plugin).gotMoney(player, amount, money));
                    }
                }

            } else {
                if (p.hasPermission("moneycard.cmd.admin")) {
                    p.getInventory().addItem(new ItemFactory(plugin).money(money, amount));
                    p.sendMessage(new ChatProcessor(plugin).buyMoney(amount, money));
                    cancel(p);
                } else {
                    if (MoneyCardPlugin.getEcon().withdrawPlayer(p, amount * money).transactionSuccess()) {
                        p.getInventory().addItem(new ItemFactory(plugin).money(money, amount));
                    } else {
                        p.sendMessage(plugin.getChat().getString("warning.BuyMoneyFailed"));
                    }
                }
            }
            cancel(p);
        }
    }

    private void cancel(Player p) {
        SetPlayer.getTarget().remove(p);
        SetMoney.getTarget().remove(p);
        player.remove(p);
    }

    public static HashSet<Player> getPlayer() {
        return player;
    }
}
