package io.github.cardsandhuskers.teams.listeners;

import io.github.cardsandhuskers.teams.handlers.TablistHandler;
import io.github.cardsandhuskers.teams.objects.Menu;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static io.github.cardsandhuskers.teams.Teams.menuList;
import static io.github.cardsandhuskers.teams.Teams.openColorInvs;


public class InventoryCloseListener implements Listener {
    private TablistHandler tablistHandler;
    Plugin plugin;

    public InventoryCloseListener(Plugin plugin) {
        this.plugin = plugin;
        tablistHandler = new TablistHandler();
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.stripColor("team menu")) || e.getView().getTitle().equalsIgnoreCase(ChatColor.stripColor("color selection"))) {
            HumanEntity p = e.getPlayer();
            Player player = (Player)p;
            ArrayList<Menu> tempMenuList = new ArrayList<>();

            tablistHandler.buildTablist();

            for (Menu m : menuList) {
                m.populateTeams();
            }
            //tablistHandler.buildTabList(player);

            for(Menu m: menuList) {
                if(m.player.equals(player)) {
                    tempMenuList.add(m);

                }
            }
            for(Menu m:tempMenuList) {
                menuList.remove(m);
            }
            if(player.getOpenInventory() == null) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                    if(openColorInvs.contains(player)) {
                        openColorInvs.remove(player);
                    }
                }, 1L);
            }
        }

    }
}
