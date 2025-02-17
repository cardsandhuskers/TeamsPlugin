package io.github.cardsandhuskers.teams.listeners;

import io.github.cardsandhuskers.teams.Teams;
import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.cardsandhuskers.teams.Teams.handler;

public class PlayerLeaveListener implements Listener {
    Teams plugin;
    public PlayerLeaveListener(Teams plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        /*Player p = e.getPlayer();
        if(handler.getPlayerTeam(p) != null) {
            Team t = handler.getPlayerTeam(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (t.getOnlinePlayers().size() == 0) {
                    handler.deleteTeam(t);
                }
            }, 10L);
        }*/
    }
}
