package io.github.cardsandhuskers.teams.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Simple holder object for each player's temp points.
 * Should probably be replaced by a HashMap eventually.
 * @author cardsandhuskers
 * @version 1.0
 */
public class TempPointsHolder {
    UUID player;
    double points;

    public TempPointsHolder(UUID p) {
        player = p;
        points = 0;
    }

    /**
     *
     * @return associated Player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public UUID getUUID() {
        return player;
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
