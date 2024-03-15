package io.github.cardsandhuskers.teams.listeners;

import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        String message = e.getMessage();
        e.setCancelled(true);
        Team senderTeam = TeamHandler.getInstance().getPlayerTeam(sender);

        String newMessage = "<";
        if(senderTeam != null) {
            newMessage += "[" + senderTeam.getColor() +
                    senderTeam.getTeamName().substring(0, Math.min(4, senderTeam.getTeamName().length())) +
                    ChatColor.RESET + "] " + senderTeam.getColor() + sender.getName() + ChatColor.RESET + "> " + message;

        }

        Bukkit.broadcastMessage(newMessage);


    }
}
