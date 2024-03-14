package io.github.cardsandhuskers.teams.objects;

import com.fasterxml.jackson.annotation.*;
import org.black_ixx.playerpoints.models.SortedPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

import static io.github.cardsandhuskers.teams.Teams.ppAPI;

/**
 * Team object that holds data and methods for each team.
 * color member is public and may be made private with a getter in the future to prevent it from being assigned
 * @author cardsandhuskers
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Team {
    private ArrayList<UUID> playerList;
    private String name;

    /**
     * this can be assigned, if you assign it something bad, things could break, so don't modify it, only read it
     * I'll deprecate this later, but I used this everywhere before I realized what I was doing, and it's gonna be a PITA to fix
     */
    public String color;

    @JsonIgnore
    private ArrayList<TempPointsHolder> tempPointsList = new ArrayList<>();

    @JsonIgnore
    private boolean ready = false;

    /**
     * returns color in the §[colorChar] format
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the color, be careful with this
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPlayerList(ArrayList<UUID> playerList) {
        this.playerList = playerList;
    }

    /*public ArrayList<UUID> getPlayerList() {
        return playerList;
    }


    public String getName() {
        return name;
    }


    public boolean getReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }*/

    /**
     * Constructor, creates the team Object
     * @param teamName name of team
     */
    public Team(String teamName) {
        //this.color = assignColor();
        name = teamName;
        playerList = new ArrayList<>();
    }

    public Team() {

    }

    @JsonCreator
    public Team(@JsonProperty("name") String name, @JsonProperty("color") String color, @JsonProperty("playerList") ArrayList<UUID> playerList) {
        this.name = name;
        this.color = color;
        this.playerList = playerList;
    }

    /**
     * Adds the specified player to the team
     * @param player player to add
     */
    public void addPlayer(Player player) {
        playerList.add(player.getUniqueId());
        tempPointsList.add(new TempPointsHolder(player.getUniqueId()));
    }

    /**
     * Removes the specified player from the team
     * @param player player to remove
     */
    public void removePlayer(Player player) {
        playerList.remove(player.getUniqueId());
    }

    /**
     * gets the list of players on the team
     * @return ArrayList of OfflinePlayers
     */
    @JsonIgnore
    public ArrayList<OfflinePlayer> getPlayers() {
        //Creates a deep copy and returns it so that no one can mess with the list
        ArrayList<OfflinePlayer> returnableList = new ArrayList<>();
        for(UUID u: playerList) {
            OfflinePlayer p = Bukkit.getPlayer(u);
            returnableList.add(p);
        }

        return returnableList;
    }

    /**
     * gets the UUIDs of all players on the team
     * @return Arraylist of UUIDs
     */
    @JsonIgnore
    public ArrayList<UUID> getPlayerIDs() {
        return new ArrayList<>(playerList);
    }

    /**
     * gets the list of all online players on the team
     * @return ArrayList of Players Deep copy of players
     */
    @JsonIgnore
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
     *
     * @return list of online players' names
     */
    @JsonIgnore
    public ArrayList<String> getOnlinePlayerNames() {
        ArrayList<String> returnableList = new ArrayList<>();
        for(Player p:getOnlinePlayers()) {
            returnableList.add(p.getName());
        }
        return returnableList;
    }

    /**
     * gets player at specified index
     * @param index
     * @return Player
     */
    @JsonIgnore
    public Player getPlayer(int index) {

        if(index < playerList.size()) {
            Player p = Bukkit.getPlayer(playerList.get(index));

            return p;
        } else {
            return null;
        }

    }

    /**
     *
     * @return name of team
     */
    @JsonIgnore
    public String getTeamName() {
        return name;
    }

    /**
     * Sorts the players by number of points and returns it, largest to smallest
     * @return arrayList of teams ordered by points
     */
    @JsonIgnore
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
     * returns the wool material representing the team's color
     * @return Material
     */
    @JsonIgnore
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
            case "§d": return Material.PINK_WOOL;
            case "§e": return Material.YELLOW_WOOL;
            default: return Material.WHITE_WOOL;
        }
    }

    /**
     * Returns the ChatColor of the team
     *
     * @return ChatColor
     */
    @JsonIgnore
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

    /**
     * Returns the Color object of the team's color
     * @return Color
     */
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
            case "§d": return Color.fromRGB(243,139,170); //pink
            case "§e": return Color.YELLOW;
            default: return Color.WHITE;
        }
    }

    /**
     * Converts color format from the § to the "&" format
     * @return String color
     */
    @JsonIgnore
    public String getConfigColor() {
        String temp = "&";
        temp += color.substring(1);
        return temp;
    }

    /**
     * gets the points the team has
     * @return int points
     */
    @JsonIgnore
    public int getPoints() {
        int points = 0;
        for (UUID u:playerList) {
            //OfflinePlayer p = Bukkit.getPlayer(u);
            points += ppAPI.look(u);
        }
        return points;
    }



    /**
     * Gets the number of players on the team
     * @return int of team size
     */
    @JsonIgnore
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
            s += playerList.get(i) + "\n";
        }
        String colorChar = color.substring(1);
        return name + "\n" + colorChar + "\n" + s;
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
     * Returns player's tempPointsHolder object
     * @param p
     * @return pointsHolder of player
     */
    @JsonIgnore
    public TempPointsHolder getPlayerTempPoints(OfflinePlayer p) {
        if(p == null) return null;
        for(TempPointsHolder h:tempPointsList) {
            if(h.getUUID().equals(p.getUniqueId())) {
                return h;
            }
        }
        return null;
    }

    /**
     * Returns player's temp points
     * @param p
     * @return double value of Player's points
     */
    @JsonIgnore
    public double getPlayerTempPointsValue(OfflinePlayer p) {
        for(TempPointsHolder h:tempPointsList) {
            if(h.getUUID().equals(p.getUniqueId())) {
                return h.getPoints();
            }
        }
        return 0;
    }

    /**
     * Gets the tempPoints for an individual player
     * @param p
     * @return temp points
     */
    @JsonIgnore
    public double getPlayerTempPointsValue(UUID p) {
        for(TempPointsHolder h:tempPointsList) {
            if(h.getUUID().equals(p)) {
                return h.getPoints();
            }
        }
        return 0;
    }


    /**
     * Gets total tempPoints for the team
     * @return points
     */
    @JsonIgnore
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
