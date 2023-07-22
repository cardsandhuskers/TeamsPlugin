package io.github.cardsandhuskers.teams.handlers;

import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TeamHandler {
    public ArrayList<String> colors = new ArrayList<>();
    private ArrayList<Team> teamList;
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

    /**
     * Creates a new team with the specified name
     * @param name
     * @return boolean of whether team could be created
     */
    public boolean createTeam(String name) {
        if(!(teamList.size() == 0)) {
            for (int i = 0; i < teamList.size(); i++) {
                if(teamList.get(i).getTeamName().equals(name)) {
                    return false;
                }
            }
        }
        Team t = new Team(name);
        //String color = t.assignColor(colors);
        //if(!(colors.isEmpty())) {
        //    colors.remove(color);
        //}
        if(!colors.isEmpty()) {
            t.color = colors.get(0);
            colors.remove(0);
        } else {
            t.color = "§f";
        }

        teamList.add(t);
        return true;
    }

    public boolean assignColor(Team t, String color) {
        if(colors.contains(color)) {
            colors.add(t.color);
            t.assignColor(color);
            colors.remove(color);
            return true;
        } else {

        }
        return false;
    }

    /**
     * Adds the specified player to the specified team
     * @param player
     * @param teamName
     * @return boolean of whether player could be added
     */
    public boolean addPlayer(Player player, String teamName) {
        for(int i = 0; i < teamList.size(); i++) {
            for(int j = 0; j < teamList.get(i).getSize(); j++) {
                if(player.equals(teamList.get(i).getPlayer(j))) {
                    if(getPlayerTeam(player).equals(getTeam(teamName))) {
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

    /**
     * removes specified player from specified team
     * @param player
     * @param team
     */
    public void removePlayer(Player player, Team team) {
        team.removePlayer(player);
        if (team.getOnlinePlayers().size() == 0) {
            deleteTeam(team);
        }
    }

    /**
     * Deletes the specified team
     * @param t
     */
    public void deleteTeam(Team t) {
        try {
            colors.add(t.color);
            teamList.remove(t);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets number of teams in array
     * @return int number of teams
     */
    public int getNumTeams() {
        return teamList.size();
    }

    /**
     * Gets team with specified name
     * @param name
     * @return Team
     */
    public Team getTeam(String name) {
        for (int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getTeamName().equals(name)) {
                return teamList.get(i);
            }
        }
        return null;
    }

    /**
     * Gets team at specified index
     * @param i
     * @return Team
     */
    public Team getTeam(int i) {
        return teamList.get(i);
    }

    /**
     * Gets team containing specified player
     * @param p
     * @return Team
     */
    public Team getPlayerTeam(Player p) {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getSize(); j++) {
                if(teamList.get(i).getPlayer(j) != null) {
                    if(teamList.get(i).getPlayer(j).equals(p)) {
                        return teamList.get(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the list of teams
     * @return ArrayList of teams
     */
    public ArrayList<Team> getTeams() {
        //Creates a deep copy and returns it so that no one can mess with the list
        ArrayList<Team> returnableList = new ArrayList<>();
        for(Team t: teamList) {
            returnableList.add(t);
        }
        return returnableList;
    }

    /**
     * Gets team that uses specified color
     * @param color
     * @return Team
     */
    public Team getTeamByColor(String color) {
        for(Team t: teamList) {
            if(t.color.equals(color)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Sorts the teams by number of points and returns it, largest to smallest
     * @return arrayList of teams ordered by points
     */
    public ArrayList<Team> getPointsSortedList() {
        ArrayList<Team> teamsArray = getTeams();
        Collections.sort(teamsArray, Comparator.comparing(Team::getPoints));
        Collections.reverse(teamsArray);
        return teamsArray;
    }

    /**
     * Gets sorted list of Teams by their temp points, largest to smallest
     * @return teamsArray
     */
    public ArrayList<Team> getTempPointsSortedList() {
        ArrayList<Team> teamsArray = getTeams();
        Collections.sort(teamsArray, Comparator.comparing(Team::getTempPoints));
        Collections.reverse(teamsArray);
        return teamsArray;
    }

    public int getTeamPoints(Team t) {
        return t.getPoints();
    }

    /**
     * Builds a string containing the name and players of all teams
     * @return String
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < teamList.size(); i++) {
            s += teamList.get(i).toString() + "\n";
        }
        return s;
    }
}
