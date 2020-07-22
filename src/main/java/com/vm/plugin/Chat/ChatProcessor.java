package com.vm.plugin.Chat;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.entity.Player;

public class ChatProcessor {

    private final MoneyCardPlugin plugin;

    public ChatProcessor(MoneyCardPlugin plugin) {
        this.plugin = plugin;
    }

    public String buyMoney(int few, int amount) {
        return plugin.getChat().getString("general.BuyMoney")
                .replaceAll("\\{few}", String.valueOf(few))
                .replaceAll("\\{amount}", String.valueOf(amount))
                .replaceAll("\\{total}", String.valueOf(few * amount));
    }

    public String giveMoney(Player player, int few, int amount) {
        return plugin.getChat().getString("general.GiveMoney")
                .replaceAll("\\{player}", player.getName())
                .replaceAll("\\{few}", String.valueOf(few))
                .replaceAll("\\{amount}", String.valueOf(amount))
                .replaceAll("\\{total}", String.valueOf(few * amount));
    }

    public String giveAllMoney(int players, int few, int amount) {
        return plugin.getChat().getString("general.GiveAllMoney")
                .replaceAll("\\{few}", String.valueOf(few))
                .replaceAll("\\{amount}", String.valueOf(amount))
                .replaceAll("\\{singleTotal}", String.valueOf(few * amount))
                .replaceAll("\\{total}", String.valueOf(few*amount*players));
    }

    public String gotMoney(Player player, int few, int amount) {
        return plugin.getChat().getString("general.GotMoney")
                .replaceAll("\\{player}", player.getName())
                .replaceAll("\\{few}", String.valueOf(few))
                .replaceAll("\\{amount}", String.valueOf(amount))
                .replaceAll("\\{total}", String.valueOf(few * amount));
    }
}
