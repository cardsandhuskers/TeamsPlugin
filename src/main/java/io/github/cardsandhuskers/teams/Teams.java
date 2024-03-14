package io.github.cardsandhuskers.teams;

import io.github.cardsandhuskers.teams.commands.*;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.listeners.InventoryCloseListener;
import io.github.cardsandhuskers.teams.listeners.PlayerLeaveListener;
import io.github.cardsandhuskers.teams.listeners.TeamMenuListener;
import io.github.cardsandhuskers.teams.objects.Menu;
import io.github.cardsandhuskers.teams.objects.Placeholder;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Teams extends JavaPlugin {
    /**
     * @deprecated - this will soon be removed, use TeamHandler.getInstance()
     */
    public static TeamHandler handler;
    public static boolean teamsLocked = false;
    public static HashMap<Player, Menu> playerMenus;

    //public static ArrayList<ServerPlayer> teamListArray = new ArrayList<>();
    public static ArrayList<Player> openColorInvs = new ArrayList<>();

    public static PlayerPointsAPI ppAPI;
    //public Menu m;
    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            ppAPI = PlayerPoints.getInstance().getAPI();
        } else {
            getLogger().severe("Player Points API is required for this plugin to work!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            new Placeholder(this).register();

        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            System.out.println("Could not find PlaceholderAPI!");
            //Bukkit.getPluginManager().disablePlugin(this);
        }

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        handler = TeamHandler.getInstance();

        getCommand("teamMenu").setExecutor(new TeamMenuCommand(handler, ppAPI));
        getCommand("lockTeams").setExecutor(new LockTeamsCommand());
        getCommand("unlockTeams").setExecutor(new UnlockTeamsCommand());
        getCommand("teams").setExecutor(new TeamCommand(this));

        getServer().getPluginManager().registerEvents(new TeamMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);

        playerMenus = new HashMap<>();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
