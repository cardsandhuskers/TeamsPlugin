package io.github.cardsandhuskers.teams.handlers;

import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;

public class TeamHandler {

    private ArrayList<String> colors;
    private ArrayList<Team> teamList;

    private static TeamHandler teamHandler = new TeamHandler();
    private TeamHandler() {
        teamList = new ArrayList<Team>();
        colors = new ArrayList<>(List.of("§2", "§3", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e"));
    }

    /**
     * Return instance of class
     */
    public static TeamHandler getInstance() {
        return teamHandler;
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

    public boolean setColor(Team t, String color) {
        if(colors.contains(color)) {
            if(t == null) return false;
            if(t.color != null) colors.add(t.color);
            t.setColor(color);
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
     * @return Team or null if team does not exist
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
     * Gets the list of team names
     *
     */
    public ArrayList<String> getTeamNames() {
        ArrayList<String> teamNames = new ArrayList<>();
        for(Team t:getTeams()) {
            teamNames.add(t.getTeamName());
        }
        return teamNames;
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

    /**
     * Overwrites teams with the inputted list of teams
     * @param teams
     */
    public void writeTeams(List<Team> teams) {
        teamList = new ArrayList<>(teams);
        colors = new ArrayList<>(List.of("§2", "§3", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e"));

        for(Team t:teams){
            colors.remove(t.getColor());
            t.resetTempPoints();
        }
    }

    /**
     *
     * @return list of unused colors
     */
    public ArrayList<String> getColors() {
        return new ArrayList<>(colors);
    }

    /**
     * Takes the colors and converts them to a list in plain text
     * @param allColors - return list of all colors or only available ones
     * @return list of colors
     */
    public ArrayList<String> getColorStrings(boolean allColors) {
        ArrayList<String> colorStrings = new ArrayList<>();
        if(allColors) {
            String[] colors = {"dark_green", "dark_aqua", "dark_purple", "gold", "gray",
                    "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow"};
            colorStrings = new ArrayList<>(List.of(colors));
        } else {
            for(String color:colors){
                switch (color) {
                    case "§2": colorStrings.add("dark_green"); break;
                    case "§3": colorStrings.add("dark_aqua"); break;
                    case "§5": colorStrings.add("dark_purple"); break;
                    case "§6": colorStrings.add("gold"); break;
                    case "§7": colorStrings.add("gray"); break;
                    case "§8": colorStrings.add("dark_gray"); break;
                    case "§9": colorStrings.add("blue"); break;
                    case "§a": colorStrings.add("green"); break;
                    case "§b": colorStrings.add("aqua"); break;
                    case "§c": colorStrings.add("red"); break;
                    case "§d": colorStrings.add("light_purple"); break;
                    case "§e": colorStrings.add("yellow"); break;
                    default: colorStrings.add("white");

                }
            }
        }

        return colorStrings;
    }

    public String convertColorString(String colorString) {
        String lowercaseColor = colorString.toLowerCase();
        switch (lowercaseColor) {
            case "dark_green": return "§2";
            case "dark_aqua": return "§3";
            case "dark_purple": return "§5";
            case "gold": return "§6";
            case "gray": return "§7";
            case "dark_gray": return "§8";
            case "blue": return "§9";
            case "green": return "§a";
            case "aqua": return "§b";
            case "red": return "§c";
            case "light_purple": return "§d";
            case "yellow": return "§e";
            default: return "§f";

        }
    }
}
