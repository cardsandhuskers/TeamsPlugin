package io.github.cardsandhuskers.teams.listeners;

import io.github.cardsandhuskers.teams.handlers.TablistHandler;
import io.github.cardsandhuskers.teams.objects.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static io.github.cardsandhuskers.teams.Teams.playerMenus;
import static io.github.cardsandhuskers.teams.Teams.openColorInvs;


public class InventoryCloseListener implements Listener {
    Plugin plugin;

    public InventoryCloseListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.stripColor("team menu"))) {
            HumanEntity p = e.getPlayer();
            Player player = (Player)p;

            TablistHandler.buildTablist();

            Menu m = playerMenus.get(p);
            if(m != null) m.close();

            if(player.getOpenInventory() == null) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                    if(openColorInvs.contains(player)) {
                        openColorInvs.remove(player);
                    }
                }, 1L);
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.stripColor("color selection"))) {
            /*for (Menu m : playerMenus.values()) {
                if(m.isOpen()) m.populateTeams();
            }*/
        }

    }
}
