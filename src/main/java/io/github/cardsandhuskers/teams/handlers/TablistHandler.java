package io.github.cardsandhuskers.teams.handlers;

import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class TablistHandler {

    public static void buildTablist() {

        /*Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for(org.bukkit.scoreboard.Team t:scoreboard.getTeams()) {
            t.unregister();
        }

        for(Team t: TeamHandler.getInstance().getTeams()) {

            org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam(t.getTeamName());
            team.setColor(t.getChatColor());
            team.setAllowFriendlyFire(false);
            for(Player p:t.getOnlinePlayers()) {
                team.addEntry(p.getDisplayName());
                team.setPrefix("[" + t.getTeamName().substring(0,1) + "] ");
            }
        }
    */
    }
}
