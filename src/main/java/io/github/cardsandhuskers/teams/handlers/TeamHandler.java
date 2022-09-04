package io.github.cardsandhuskers.teams.handlers;

import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class TeamHandler {
    ArrayList<String> colors = new ArrayList<>();
    ArrayList<Team> teamList;
    public TeamHandler() {
        teamList = new ArrayList<Team>();
        colors.add("§2");
        colors.add("§3");
        colors.add("§5");
        colors.add("§6");
        colors.add("§7");
        colors.add("§8");
        colors.add("§9");
        colors.add("§a");
        colors.add("§b");
        colors.add("§c");
        colors.add("§d");
        colors.add("§e");
    }

    public boolean createTeam(String name) {
        if(!(teamList.size() == 0)) {
            for (int i = 0; i < teamList.size(); i++) {
                if(teamList.get(i).getTeamName().equals(name)) {
                    return false;
                }
            }
        }
        Team t = new Team(name);



        String color = t.assignColor(colors);
        if(!(colors.isEmpty())) {
            colors.remove(color);
        }


        teamList.add(t);
        return true;
    }

    public boolean addPlayer(Player player, String teamName) {
        for(int i = 0; i < teamList.size(); i++) {
            for(int j = 0; j < teamList.get(i).getSize(); j++) {
                if(player.equals(teamList.get(i).getPlayer(j))) {
                    if(getPlayerTeam(player).equals(getTeam(teamName))) {
                        player.sendMessage("You are Already on this Team");
                        return false;
                    } else {
                        removePlayer(player, getPlayerTeam(player));
                        getTeam(teamName).addPlayer(player);
                        return true;
                    }

                }
            }
        }
        getTeam(teamName).addPlayer(player);
        return true;
    }

    public void removePlayer(Player player, Team team) {
        team.removePlayer(player);
        if (team.getSize() == 0) {
            deleteTeam(team);
        }
    }

    public void deleteTeam(Team t) {
        colors.add(t.color);
        teamList.remove(t);
    }

    public int getNumTeams() {
        return teamList.size();
    }

    public Team getTeam(String name) {
        for (int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getTeamName().equals(name)) {
                return teamList.get(i);
            }
        }
        return null;
    }
    public Team getTeam(int i) {
        return teamList.get(i);
    }

    public Team getPlayerTeam(Player p) {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getSize(); j++) {
                if(teamList.get(i).getPlayer(j).equals(p)) {
                    return teamList.get(i);
                }
            }
        }
        return null;
    }


    public ArrayList<Team> getTeams() {
        return teamList;
    }



    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < teamList.size(); i++) {
            s += teamList.get(i).toString() + "\n";
        }
        return s;
    }
}
