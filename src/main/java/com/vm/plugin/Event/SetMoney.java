package com.vm.plugin.Event;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.HashSet;

public class SetMoney implements Listener {

    private final MoneyCardPlugin plugin;
    private static HashSet<Player> player;
    private static HashMap<Player, Integer> target;

    public SetMoney(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        player = new HashSet<>();
        target = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (player.contains(e.getPlayer())) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(plugin.getChat().getString("general.Canceled"));
                SetPlayer.getTarget().remove(p);
                player.remove(p);
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(e.getMessage());
                if (amount < 0) {
                    throw new NumberFormatException();
                }
                target.put(p, amount);
            } catch (NumberFormatException exception) {
                p.sendMessage(plugin.getChat().getString("warning.NotInt"));
                return;
            }

            p.sendMessage(plugin.getChat().getString("general.HowMany"));
            player.remove(p);
            SetAmount.getPlayer().add(p);
        }
    }

    public static HashSet<Player> getPlayer() {
        return player;
    }

    public static HashMap<Player, Integer> getTarget() {
        return target;
    }
}
