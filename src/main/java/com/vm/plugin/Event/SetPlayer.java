package com.vm.plugin.Event;

import com.vm.plugin.MoneyCardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.HashSet;

public class SetPlayer implements Listener {

    private final MoneyCardPlugin plugin;
    private static HashSet<Player> player;
    private static HashMap<Player, Player> target;

    public SetPlayer(MoneyCardPlugin plugin) {
        this.plugin = plugin;
        player = new HashSet<>();
        target = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (player.contains(p)) {
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(plugin.getChat().getString("general.Canceled"));
                player.remove(p);
                return;
            } else if (e.getMessage().equalsIgnoreCase("all*")) {
                p.sendMessage(plugin.getChat().getString("general.SetAllPlayer"));
                p.sendMessage(plugin.getChat().getString("general.HowMuch"));
                player.remove(p);
                target.put(p, null);
                SetMoney.getPlayer().add(p);
                return;
            }

            Player target = Bukkit.getPlayerExact(e.getMessage());
            if (Bukkit.getPlayerExact(e.getMessage()) != null) {
                p.sendMessage(plugin.getChat().getString("general.HowMuch"));
                SetPlayer.target.put(p, target);
                player.remove(p);
                SetMoney.getPlayer().add(p);
            } else {
                p.sendMessage(plugin.getChat().getString("warning.PlayerNotFound"));
            }
        }
    }

    public static HashSet<Player> getPlayer() {
        return player;
    }

    public static HashMap<Player, Player> getTarget() {
        return target;
    }
}
