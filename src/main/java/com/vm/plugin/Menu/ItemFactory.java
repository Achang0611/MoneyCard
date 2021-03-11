package com.vm.plugin.Menu;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

    private final MoneyCardPlugin plugin;

    public ItemFactory(MoneyCardPlugin plugin) {
        this.plugin = plugin;
    }

    private ItemStack model(String slot) {
        Material material = Material.getMaterial(
                plugin.getMenu().getString(slot + ".material").toUpperCase());
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();

        String name = plugin.getMenu().getString(slot + ".name");
        String amount = String.valueOf(plugin.getMenu().getInt(slot + ".amount"));
        meta.setDisplayName(name.replaceAll("\\{amount}", amount));
        List<String> lore = plugin.getMenu().getStringList(slot + ".lore");
        lore.replaceAll(s -> s.replaceAll("\\{amount}", amount));
        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public ItemStack getGlass() {
        ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName("§r");
        stack.setItemMeta(meta);

        return stack;
    }

    public ItemStack getSlot(int i) {
        if (i < 0 || i > 6) {
            return null;
        }
        return model("slot" + i);
    }

    public ArrayList<ItemStack> getAllItemList() {
        ArrayList<ItemStack> allItem = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            allItem.add(getSlot(i));
        }
        allItem.add(getGlass());
        return allItem;
    }

    public ItemStack getMoney(int slot, int amount) {
        return money(plugin.getMenu().getInt("slot" + slot + ".amount"), amount);
    }

    public ItemStack money(int amounts, int itemAmount) {
        Material card = Material.getMaterial(plugin.getMenu().getString("card.material").toUpperCase());
        ItemStack stack = new ItemStack(card, itemAmount);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(
                plugin.getMenu().getString("card.name")
                        .replaceAll("\\{amount}", String.valueOf(amounts)));
        List<String> lore = plugin.getMenu().getStringList("card.lore");
        lore.replaceAll(s -> s.replaceAll("\\{amount}", String.valueOf(amounts)));
        meta.setLore(lore);
        meta.setLocalizedName("MoneyCard{amount: " + amounts + "}");
        stack.setItemMeta(meta);

        return stack;
    }
}
