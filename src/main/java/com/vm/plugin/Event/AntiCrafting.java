package com.vm.plugin.Event;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class AntiCrafting implements Listener {

    private final MoneyCardPlugin plugin;

    public AntiCrafting(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent e) {
        List<ItemStack> matrix = Arrays.asList(e.getInventory().getMatrix());
        if (anyCard(matrix)) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    private boolean anyCard(List<ItemStack> stacks) {
        for (ItemStack stack :
                stacks) {
            if (stack == null) {
                continue;
            }
            if (!stack.hasItemMeta()) {
                continue;
            }
            if (!stack.getItemMeta().hasLocalizedName()) {
                continue;
            }
            if (plugin.CardMatcher(stack.getItemMeta().getLocalizedName()).find()) {
                return true;
            }
        }
        return false;
    }
}