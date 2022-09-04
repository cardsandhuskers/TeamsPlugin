package io.github.cardsandhuskers.teams.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Team {
    ArrayList<Player> teamList;
    String name;

    public String color;
    boolean ready = false;

    public Team(String teamName) {
        //this.color = assignColor();
        name = teamName;
        teamList = new ArrayList<Player>();
    }
    public void addPlayer(Player player) {
        teamList.add(player);
    }

    //finds player on team and removes them
    public void removePlayer(Player player) {
        teamList.remove(player);
    }

    //gets list of players on team
    public ArrayList<Player> getPlayers() {
        return teamList;
    }
    public Player getPlayer(int i) {
        return teamList.get(i);
    }


    //gets name of team
    public String getTeamName() {
        return name;
    }


    public Material getWoolColor() {
        switch(color) {
            case "§2": return Material.GREEN_WOOL;
            case "§3": return Material.CYAN_WOOL;
            case "§5": return Material.PURPLE_WOOL;
            case "§6": return Material.ORANGE_WOOL;
            case "§7": return Material.LIGHT_GRAY_WOOL;
            case "§8": return Material.GRAY_WOOL;
            case "§9": return Material.BLUE_WOOL;
            case "§a": return Material.LIME_WOOL;
            case"§b": return Material.LIGHT_BLUE_WOOL;
            case "§c": return Material.RED_WOOL;
            case "§d": return Material.MAGENTA_WOOL;
            case "§e": return Material.YELLOW_WOOL;
            default: return Material.WHITE_WOOL;
        }
    }

    public String assignColor(ArrayList<String> colors) {
        if(!(colors.isEmpty())) {
            String tempColor;

            Random r = new Random();
            int number = r.nextInt(colors.size());

            tempColor = colors.get(number);
            color = tempColor;
            return tempColor;
        } else {
            color = "§f";
            return "§f";
        }

    }




    //get size of team
    public int getSize() {
        if (teamList.isEmpty()) {
            return 0;
        } else {
            int size = 0;
            for (Player p : teamList) {
                size++;
            }
            return size;
        }
    }
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < teamList.size(); i++) {
            s += teamList.get(i) + " ";
        }
        return name + ": " + s;
    }
    public void toggleReady() {
        if(ready == false) {
            ready = true;
        } else {
            ready = false;
        }
    }
    public boolean isReady() {
        return ready;
    }
}
