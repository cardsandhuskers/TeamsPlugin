package io.github.cardsandhuskers.teams.commands;

import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.objects.Menu;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.cardsandhuskers.teams.Teams.playerMenus;
import static io.github.cardsandhuskers.teams.Teams.teamsLocked;

public class TeamMenuCommand implements CommandExecutor {
    TeamHandler handler;

    public TeamMenuCommand(TeamHandler h, PlayerPointsAPI ppAPI) {
        handler = h;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!teamsLocked || p.isOp()) {
            /*
            */
            if(!playerMenus.containsKey(p)) {
                Menu m = new Menu(p);
                m.open();
                m.generateMenu();
                playerMenus.put(p, m);
            } else {
                playerMenus.get(p).generateMenu();
            }


        } else {
            p.sendMessage("Teams are locked. You are no longer allowed to make changes.");
        }

        return false;
    }
}
