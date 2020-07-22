package com.vm.plugin.Menu;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MenuModel {

    private final MoneyCardPlugin plugin;
    private final ItemCreator creator;
    private String title = "ERROR: 錯誤的選單名稱";
    private Inventory inv;


    public MenuModel(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        creator = new ItemCreator(plugin);
    }

    public void create() {
        inv = Bukkit.createInventory(null, 27, title);
        line();
        button();
    }

    private void line() {
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, creator.getGlass());
            inv.setItem(i + 9, creator.getGlass());
            inv.setItem(i + 18, creator.getGlass());
        }
    }

    private void button() {
        int c = 1;
        for (int i = 9; i < 18; i += 2) {
            inv.setItem(i, creator.getSlot(c));
            c++;
        }
    }

    public Inventory forPlayer() {
        title = plugin.getMenu().getString("title.player");
        create();
        return inv;
    }

    public Inventory forAdmin() {
        title = plugin.getMenu().getString("title.admin");
        create();
        inv.setItem(26, creator.getSlot(6));
        return inv;
    }
}
