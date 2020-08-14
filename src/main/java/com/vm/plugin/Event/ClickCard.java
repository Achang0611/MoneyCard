package com.vm.plugin.Event;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;

public class ClickCard implements Listener {

    private final MoneyCardPlugin plugin;

    public ClickCard(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        ItemStack item = e.getItem();
        String localName = item.getItemMeta().getLocalizedName();
        if (localName == null) {
            return;
        }

        Matcher matcher = plugin.CardMatcher(localName);
        if (!matcher.find()) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            int amount = Integer.parseInt(matcher.group(1));
            String chat = plugin.getChat().getString("general.GetMoney");

            if (p.isSneaking()) {
                if (!p.hasPermission("moneycard.use.shift")) {
                    p.sendMessage(plugin.getChat().getString("warning.NoPermission"));
                    return;
                }

                if (MoneyCardPlugin.getEcon().depositPlayer(p, amount * item.getAmount())
                        .transactionSuccess()) {
                    chat = chat.replaceAll("\\{amount}", String.valueOf(amount * item.getAmount()));
                    p.getInventory().getItemInMainHand().setAmount(0);
                    p.sendMessage(chat);
                } else {
                    p.sendMessage(plugin.getChat().getString("error.GetMoneyFailed"));
                }
            } else {
                if (!p.hasPermission("moneycard.use.normal")) {
                    p.sendMessage(plugin.getChat().getString("warning.NoPermission"));
                    return;
                }

                if (MoneyCardPlugin.getEcon().depositPlayer(p, amount).transactionSuccess()) {
                    chat = chat.replaceAll("\\{amount}", String.valueOf(amount));
                    p.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
                    p.sendMessage(chat);
                } else {
                    p.sendMessage(plugin.getChat().getString("error.GetMoneyFailed"));
                }
            }
        }
    }
}
