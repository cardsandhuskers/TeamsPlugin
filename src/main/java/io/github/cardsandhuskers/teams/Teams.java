package io.github.cardsandhuskers.teams;

import io.github.cardsandhuskers.teams.commands.LockTeamsCommand;
import io.github.cardsandhuskers.teams.commands.TeamCommand;
import io.github.cardsandhuskers.teams.commands.UnlockTeamsCommand;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.listeners.InventoryCloseListener;
import io.github.cardsandhuskers.teams.listeners.TeamMenuListener;
import io.github.cardsandhuskers.teams.objects.Menu;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Teams extends JavaPlugin {
    public static TeamHandler handler;

    public static boolean teamsLocked = false;
    public static ArrayList<Menu> menuList;
    //public Menu m;
    @Override
    public void onEnable() {
        // Plugin startup logic
        handler = new TeamHandler();
        //m = new Menu(handler);
        getCommand("team").setExecutor(new TeamCommand(handler));
        getCommand("lockTeams").setExecutor(new LockTeamsCommand());
        getCommand("unlockTeams").setExecutor(new UnlockTeamsCommand());
        getServer().getPluginManager().registerEvents(new TeamMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);

        menuList = new ArrayList<>();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
