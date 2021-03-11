package com.vm.plugin.Event;

import com.vm.plugin.Chat.ChatProcessor;
import com.vm.plugin.Menu.ItemFactory;
import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ClickMenu implements Listener {

    private final MoneyCardPlugin plugin;

    public ClickMenu(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        String playerTitle, adminTitle;
        playerTitle = plugin.getMenu().getString("title.player");
        adminTitle = plugin.getMenu().getString("title.admin");
        String title = e.getView().getTitle();

        ItemStack item = e.getCurrentItem();
        Player p = ((Player) e.getWhoClicked());

        ArrayList<ItemStack> button = new ItemFactory(plugin).getAllItemList();

        if (button.contains(item)) {
            e.setCancelled(true);
        } else {
            return;
        }

        int slot = getClickSlot(item);
        int amount = plugin.getMenu().getInt("slot" + slot + ".amount");
        int few = 1;

        if (title.equals(playerTitle)) {
            if (slot <= 4) {
                if (isInvFull(p)) {
                    p.sendMessage(plugin.getChat().getString("warning.FullInventory"));
                    return;
                }

                if (MoneyCardPlugin.getEcon()
                        .withdrawPlayer(p, amount)
                        .transactionSuccess()) {
                    p.getInventory().addItem(new ItemFactory(plugin).getMoney(slot, few));
                    p.sendMessage(new ChatProcessor(plugin).buyMoney(few, amount));
                } else {
                    p.sendMessage(plugin.getChat().getString("warning.BuyMoneyFailed"));
                }
            } else if (slot == 5) {
                if (isInvFull(p)) {
                    p.sendMessage(plugin.getChat().getString("warning.FullInventory"));
                    return;
                }
                p.closeInventory();
                SetMoney.getPlayer().add(p);
                p.sendMessage(plugin.getChat().getString("general.HowMuch"));
            }
        } else if (title.equals(adminTitle)) {
            if (slot <= 4) {
                if (isInvFull(p)) {
                    p.sendMessage(plugin.getChat().getString("warning.FullInventory"));
                    return;
                }
                p.getInventory().addItem(new ItemFactory(plugin).getMoney(slot, few));
                p.sendMessage(new ChatProcessor(plugin).buyMoney(few, amount));
            } else if (slot == 5) {
                if (isInvFull(p)) {
                    p.sendMessage(plugin.getChat().getString("warning.FullInventory"));
                    return;
                }
                p.closeInventory();
                SetMoney.getPlayer().add(p);
                p.sendMessage(plugin.getChat().getString("general.HowMuch"));
            } else if (slot == 6) {
                p.closeInventory();
                SetPlayer.getPlayer().add(p);
                p.sendMessage(plugin.getChat().getString("general.Who"));
            }
        }
    }

    private boolean isInvFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

    private int getClickSlot(ItemStack item) {
        ArrayList<ItemStack> button = new ItemFactory(plugin).getAllItemList();

        for (int i = 0; i < 6; i++) {
            if (button.get(i).equals(item)) {
                return i + 1;
            }
        }
        return 404;
    }
}