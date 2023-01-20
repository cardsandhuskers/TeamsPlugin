package io.github.cardsandhuskers.teams.objects;

import org.black_ixx.playerpoints.models.SortedPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

import static io.github.cardsandhuskers.teams.Teams.ppAPI;

public class Team {
    private ArrayList<UUID> playerList;
    private final String name;
    public String color;

    private ArrayList<TempPointsHolder> tempPointsList = new ArrayList<>();

    private boolean ready = false;

    /**
     * Constructor, creates the team Object
     * @param teamName
     */
    public Team(String teamName) {
        //this.color = assignColor();
        name = teamName;
        playerList = new ArrayList<>();
    }

    /**
     * Adds the specified player to the team
     * @param player
     */
    public void addPlayer(Player player) {
        playerList.add(player.getUniqueId());
        tempPointsList.add(new TempPointsHolder(player.getUniqueId()));
    }

    /**
     * Removes the specified player from the team
     * @param player
     */
    public void removePlayer(Player player) {
        playerList.remove(player.getUniqueId());
    }

    /**
     * gets the list of players on the team
     * @return ArrayList of Players
     */
    public ArrayList<OfflinePlayer> getPlayers() {
        //Creates a deep copy and returns it so that no one can mess with the list
        ArrayList<OfflinePlayer> returnableList = new ArrayList<>();
        for(UUID u: playerList) {
            OfflinePlayer p = Bukkit.getPlayer(u);
            returnableList.add(p);
        }

        return returnableList;
    }

    public ArrayList<Player> getOnlinePlayers() {
        //Creates a deep copy and returns it so that no one can mess with the list
        ArrayList<Player> returnableList = new ArrayList<>();
        for(UUID u: playerList) {
            Player p = Bukkit.getPlayer(u);
            if(p!= null) {
                returnableList.add(p);
            }
        }

        return returnableList;
    }

    /**
     * gets player at specified index
     * @param index
     * @return Player
     */
    public Player getPlayer(int index) {

        if(index < playerList.size()) {
            Player p = Bukkit.getPlayer(playerList.get(index));

            return p;
        } else {
            return null;
        }

    }

    //gets name of team
    public String getTeamName() {
        return name;
    }

    /**
     * returns the wool material representing the team's color
     * @return Material
     */
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
            case "§b": return Material.LIGHT_BLUE_WOOL;
            case "§c": return Material.RED_WOOL;
            case "§d": return Material.MAGENTA_WOOL;
            case "§e": return Material.YELLOW_WOOL;
            default: return Material.WHITE_WOOL;
        }
    }

    /**
     * Sorts the players by number of points and returns it, largest to smallest
     * @return arrayList of teams ordered by points
     */
    public ArrayList<Player> getPointsSortedList() {
        List<SortedPlayer> playerArray = ppAPI.getTopSortedPoints();
        ArrayList<Player> finalOrder = new ArrayList<>();
        for(SortedPlayer p:playerArray) {
            for(Player player:getOnlinePlayers()) {
                if(p.getUniqueId().equals(player.getUniqueId())) {
                    finalOrder.add(player);
                }
            }
        }
        return finalOrder;
    }

    /**
     * Returns the Color type color for the team
     *
     * @return Color
     */
    public ChatColor getChatColor() {
        switch (color) {
            case "§2": return ChatColor.DARK_GREEN;
            case "§3": return ChatColor.DARK_AQUA;
            case "§5": return ChatColor.DARK_PURPLE;
            case "§6": return ChatColor.GOLD;
            case "§7": return ChatColor.GRAY; //light gray
            case "§8": return ChatColor.DARK_GRAY;
            case "§9": return ChatColor.BLUE;
            case "§a": return ChatColor.GREEN;
            case "§b": return ChatColor.AQUA;
            case "§c": return ChatColor.RED;
            case "§d": return ChatColor.LIGHT_PURPLE; //magenta
            case "§e": return ChatColor.YELLOW;
            default: return ChatColor.WHITE;
        }
    }
    public Color translateColor() {
        switch (color) {
            case "§2": return Color.GREEN;
            case "§3": return Color.TEAL;
            case "§5": return Color.PURPLE;
            case "§6": return Color.ORANGE;
            case "§7": return Color.fromRGB(145,145,145); //light gray
            case "§8": return Color.GRAY;
            case "§9": return Color.BLUE;
            case "§a": return Color.LIME;
            case "§b": return Color.AQUA;
            case "§c": return Color.RED;
            case "§d": return Color.fromRGB(255,0,255); //magenta
            case "§e": return Color.YELLOW;
            default: return Color.WHITE;
        }
    }

    /**
     * Converts color format from the § to the & format
     * @return String color
     */
    public String getConfigColor() {
        String temp = "&";
        temp += color.substring(1);
        return temp;
    }

    /**
     * gets the points the team has
     * @return int points
     */
    public int getPoints() {
        int points = 0;
        for (UUID u:playerList) {
            //OfflinePlayer p = Bukkit.getPlayer(u);
            points += ppAPI.look(u);
        }
        return points;
    }

    /**
     * Takes in the list of colors and assigns a random one to the team
     * @param color
     * @return String of color
     */
    public void assignColor(String color) {
        /*
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

         */
        this.color = color;
    }

    /**
     * Gets the number of players on the team
     * @return int of team size
     */
    public int getSize() {
        if (playerList.isEmpty()) {
            return 0;
        } else {
            int size = 0;
            for (UUID u : playerList) {
                size++;
            }
            return size;
        }
    }

    /**
     * Converts the name of the team and its players to a string
     * @return string
     */
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < playerList.size(); i++) {
            s += playerList.get(i) + " ";
        }
        return name + ": " + s;
    }

    /**
     * Toggles whether the team is ready
     */
    public void toggleReady() {
        if(ready == false) {
            ready = true;
        } else {
            ready = false;
        }
    }

    /**
     * returns whether the team is ready
     * @return boolean
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Initializes the tempPoints list for all players on the team
     */
    private void initTempPoints () {
        for(UUID p:playerList) {
            tempPointsList.add(new TempPointsHolder(p));
        }
    }

    /**
     * Resets the tempPoints values for every player on the team
     */
    public void resetTempPoints() {
        tempPointsList.clear();
        initTempPoints();
    }

    /**
     * Returns tempPoints of specified player
     * @param p
     * @return points
     */
    public TempPointsHolder getPlayerTempPoints(OfflinePlayer p) {
        for(TempPointsHolder h:tempPointsList) {
            if(h.getPlayer() != null && p.getPlayer() != null) {
                if(h.getPlayer().equals(p)) {
                    return h;
                }
            }
        }
        return null;
    }

    public double getPlayerTempPointsValue(OfflinePlayer p) {
        for(TempPointsHolder h:tempPointsList) {
            if(h.getPlayer().equals(p)) {
                return h.getPoints();
            }
        }
        return 0;
    }

    /**
     * Gets total tempPoints for the team
     * @return points
     */
    public double getTempPoints() {
        double points = 0;
        for(TempPointsHolder h:tempPointsList) {
            points += h.getPoints();
        }
        return points;
    }

    /**
     * adds specified points to specified player
     * @param p
     * @param points
     */
    public void addTempPoints(Player p, double points) {
        for(TempPointsHolder h:tempPointsList) {
            if(h.getPlayer() != null) {
                if(h.getPlayer().equals(p)) {
                    h.addPoints(points);
                }
            }
        }
    }
}
