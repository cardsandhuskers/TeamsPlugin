package io.github.cardsandhuskers.teams.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.cardsandhuskers.teams.Teams;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.cardsandhuskers.teams.Teams.teamsLocked;


public class TeamCommand implements TabExecutor {
    private Teams plugin;
    public TeamCommand(Teams plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0 && (!(sender instanceof Player) || sender.isOp())) {

            sender.sendMessage("Usage: " +
                    "\n/teams add [teamName] [color]" +
                    "\n/teams remove [teamName]" +
                    "\n/teams addPlayer [teamName] [player]" +
                    "\nremovePlayer [player]" +
                    "\njoin [teamName]" +
                    "\nleave" +
                    "\nsetColor [teamName] [color]" +
                    "\nsave" +
                    "\nload");
        } else if (args.length == 0){
            sender.sendMessage("Usage: " +
                    "\njoin [teamName]" +
                    "\nleave");
        }

        //{"Add", "Remove", "AddPlayer", "RemovePlayer", "Join", "Leave", "SetColor"}
        TeamHandler handler = TeamHandler.getInstance();
        if(args[0].equalsIgnoreCase("Add") && (!(sender instanceof Player) || sender.isOp())) return addTeam(sender, args);
        else if(args[0].equalsIgnoreCase("Remove") && (!(sender instanceof Player) || sender.isOp())) return removeTeam(sender, args);
        else if(args[0].equalsIgnoreCase("AddPlayer") && (!(sender instanceof Player) || sender.isOp())) return addPlayer(sender, args);
        else if(args[0].equalsIgnoreCase("RemovePlayer") && (!(sender instanceof Player) || sender.isOp())) return removePlayer(sender, args);
        else if(args[0].equalsIgnoreCase("Join") && sender instanceof Player) return join(sender, args);
        else if(args[0].equalsIgnoreCase("Leave") && sender instanceof Player) return leave(sender, args);
        else if(args[0].equalsIgnoreCase("SetColor") && (!(sender instanceof Player) || sender.isOp())) return setColor(sender, args);
        else if(args[0].equalsIgnoreCase("save") && (!(sender instanceof Player) || sender.isOp())) return saveTeams();
        else if(args[0].equalsIgnoreCase("load") && (!(sender instanceof Player) || sender.isOp())) return loadTeams();
        else {
            //invalid usage or argument type
            sender.sendMessage(ChatColor.RED + "ERROR: Invalid Usage");
        }


        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TeamHandler handler = TeamHandler.getInstance();

        if(args.length == 1) {
            if (sender instanceof Player p && p.isOp()) {
                return new ArrayList<>(List.of(new String[]{"add", "remove", "addPlayer", "removePlayer", "join", "leave", "setColor", "save", "load"}));
            } else {
                return new ArrayList<>(List.of(new String[]{"Join", "Leave"}));
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove") ||
                    args[0].equalsIgnoreCase("addplayer") ||
                    //args[0].equalsIgnoreCase("removeplayer") ||
                    args[0].equalsIgnoreCase("join") ||
                    args[0].equalsIgnoreCase("setcolor")) {

                return handler.getTeamNames();
            }
            if(args[0].equalsIgnoreCase("removeplayer")) {
                ArrayList<String> playerNames = new ArrayList<>();
                for(Player p:Bukkit.getOnlinePlayers()) {
                    playerNames.add(p.getDisplayName());
                }
                return playerNames;
            }
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("setcolor")) {
                return handler.getColorStrings(false);
            }

        }

        return null;
    }

    private boolean addTeam(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }

        String color = handler.convertColorString(args[2]);
        if(color.equals("§f")) {
            sender.sendMessage(ChatColor.RED + "ERROR: Invalid Color!");
            return false;
        }

        boolean creatable = handler.createTeam(args[1]);
        if(! creatable) {
            sender.sendMessage(ChatColor.RED + "ERROR: Team already exists. Use /teams setColor to change its color!");
            return false;
        }

        Team team = handler.getTeam(args[1]);
        handler.setColor(team, color);
        sender.sendMessage(ChatColor.GREEN + "Team " + team.getChatColor() + team.getTeamName() + ChatColor.RESET + ChatColor.GREEN + " has been created.");
        return true;
    }

    private boolean removeTeam(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }

        Team t = handler.getTeam(args[1]);
        if(t == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Team " + ChatColor.RESET + args[1] + ChatColor.RED + " does not exist!");
            return false;
        } else {
            handler.deleteTeam(t);
            sender.sendMessage(ChatColor.GREEN + "Deleted team " + ChatColor.RESET + args[1]);
        }
        return true;
    }

    private boolean addPlayer(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }

        Team team = handler.getTeam(args[1]);
        if(team == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Team " + ChatColor.RESET + args[1] + ChatColor.RED + " does not exist");
            return false;
        }

        Player p = Bukkit.getPlayer(args[2]);
        if(p == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Player " + ChatColor.RESET + args[2] + ChatColor.RED + " does not exist");
            return false;
        }

        if(handler.getPlayerTeam(p) == team) {
            sender.sendMessage(ChatColor.RED + "ERROR: Player " + ChatColor.RESET + p.getDisplayName() + ChatColor.RED + " is already on " + team.getChatColor() + team.getTeamName());
            return false;
        } else {
            if(handler.getPlayerTeam(p) != null) {
                handler.getPlayerTeam(p).removePlayer(p);
            }
            team.addPlayer(p);
            sender.sendMessage(ChatColor.GREEN + "Player " + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + " was added to " + team.getChatColor() + team.getTeamName());
        }
        return true;
    }

    private boolean removePlayer(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }
        Player p = Bukkit.getPlayer(args[1]);
        if(p == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Player " + ChatColor.RESET + args[1] + ChatColor.RED + " does not exist");
            return false;
        }
        Team t = handler.getPlayerTeam(p);
        if(t == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Player " + ChatColor.RESET + args[1] + ChatColor.RED + " is not on a team!");
            return false;
        } else {
            t.removePlayer(p);
            sender.sendMessage(ChatColor.GREEN + "Player " + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + " successfully removed from " + t.getChatColor() + t.getTeamName());
        }
        return true;
    }

    private boolean join(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        Player p = (Player) sender;
        if(teamsLocked && !p.isOp()) {
            sender.sendMessage(ChatColor.RED + "ERROR: Teams are Locked!");
        }

        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }
        Team team = handler.getTeam(args[1]);
        if(team == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Team " + ChatColor.RESET + args[1] + ChatColor.RED + " does not exist");
            return false;
        }
        if(handler.getPlayerTeam(p) != null) {
            handler.getPlayerTeam(p).removePlayer(p);
        }

        team.addPlayer(p);
        sender.sendMessage(ChatColor.GREEN + "Successfully Joined " + team.getChatColor() + team.getTeamName());
        return true;
    }
    private boolean leave(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        Player p = (Player) sender;
        if(teamsLocked && !p.isOp()) {
            sender.sendMessage(ChatColor.RED + "ERROR: Teams are Locked!");
        }

        Team team = handler.getPlayerTeam(p);
        if(team == null) {
            p.sendMessage(ChatColor.RED + "ERROR: You are not on a team.");
            return  false;
        }
        team.removePlayer(p);
        p.sendMessage(ChatColor.GREEN + "Successfully left " + team.getChatColor() + team.getTeamName());
        return  true;
    }

    private boolean setColor(CommandSender sender, String[] args) {
        TeamHandler handler = TeamHandler.getInstance();
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "ERROR: Missing Arguments!");
            return false;
        }
        Team team = handler.getTeam(args[1]);
        if(team == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: Team " + ChatColor.RESET + args[1] + ChatColor.RED + " does not exist!");
            return false;
        }

        String color = handler.convertColorString(args[2]);
        if(color.equals("§f")) {
            sender.sendMessage(ChatColor.RED + "ERROR: Invalid Color!");
            return false;
        }

        handler.setColor(team, color);
        sender.sendMessage(ChatColor.GREEN + "Updated team color for " + team.getChatColor() + team.getTeamName());
        return true;

    }
    private boolean saveTeams() {

        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/teams.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ArrayList<String> teams = new ArrayList<>();
        try {
            for(Team t:TeamHandler.getInstance().getTeams()) {
                teams.add(objectMapper.writeValueAsString(t));
            }
            objectMapper.writeValue(file, teams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private boolean loadTeams() {
        try {
            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/teams.json");
            ObjectMapper objectMapper = new ObjectMapper();

            TypeFactory typeFactory = objectMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(List.class, String.class);

            // Read the JSON array as a list of strings
            List<String> jsonStrings = objectMapper.readValue(file, collectionType);

            ArrayList<Team> teams = new ArrayList<>();
            for (String jsonString : jsonStrings) {
                Team team = objectMapper.readValue(jsonString, Team.class);
                teams.add(team);
            }

            TeamHandler.getInstance().writeTeams(teams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


}
