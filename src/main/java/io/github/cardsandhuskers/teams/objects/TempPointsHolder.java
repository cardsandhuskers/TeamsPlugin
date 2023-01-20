package io.github.cardsandhuskers.teams.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempPointsHolder {
    UUID player;
    double points;

    public TempPointsHolder(UUID p) {
        player = p;
        points = 0;
    }

    /**
     *
     * @return Player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    /**
     *
     * @return int points
     */
    public double getPoints() {
        return points;
    }

    /**
     *
     * @param points
     */
    public void addPoints(double points) {
        this.points += points;
    }
}
