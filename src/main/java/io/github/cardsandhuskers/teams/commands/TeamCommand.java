package io.github.cardsandhuskers.teams.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.cardsandhuskers.teams.Teams;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.stream.Collectors;

import static io.github.cardsandhuskers.teams.Teams.teamsLocked;


public class TeamCommand implements TabExecutor {
    private Teams plugin;
    public TeamCommand(Teams plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0 && (!(sender instanceof Player) || sender.isOp())) {

            sender.sendMessage("""
                    Usage:\s
                    /teams add [teamName] [color]
                    /teams remove [teamName]
                    /teams addPlayer [teamName] [player]
                    removePlayer [player]
                    join [teamName]
                    leave
                    setColor [teamName] [color]
                    save
                    load""");
            return true;
        } else if (args.length == 0){
            sender.sendMessage("""
                    Usage:\s
                    join [teamName]
                    leave""");
            return true;
        }

        ArrayList<String> newArgs = new ArrayList<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].length() > 0 && args[i].charAt(0) == '"') {

                boolean endFound = false;
                String combinedArg = "";

                for(int j = i; j < args.length; j++) {
                    combinedArg+= args[j] + " ";
                    if(args[j].length() >= 2 && args[j].charAt(args[j].length()-1) == '"') {
                        //endpoint has been reached
                        endFound = true;
                        combinedArg = combinedArg.substring(1, combinedArg.length()-2);
                        newArgs.add(combinedArg);
                        i = j;
                        break;
                    }
                }
            } else {
                newArgs.add(args[i]);
            }
        }
        args = newArgs.toArray(new String[0]);

        //{"Add", "Remove", "AddPlayer", "RemovePlayer", "Join", "Leave", "SetColor"}
        TeamHandler handler = TeamHandler.getInstance();
        if(args[0].equalsIgnoreCase("Add") && (!(sender instanceof Player) || sender.isOp())) return addTeam(sender, args);
        else if(args[0].equalsIgnoreCase("Remove") && (!(sender instanceof Player) || sender.isOp())) return removeTeam(sender, args);
        else if(args[0].equalsIgnoreCase("AddPlayer") && (!(sender instanceof Player) || sender.isOp())) return addPlayer(sender, args);
        else if(args[0].equalsIgnoreCase("RemovePlayer") && (!(sender instanceof Player) || sender.isOp())) return removePlayer(sender, args);
        else if(args[0].equalsIgnoreCase("Join") && sender instanceof Player) return join(sender, args);
        else if(args[0].equalsIgnoreCase("Leave") && sender instanceof Player) return leave(sender, args);
        else if(args[0].equalsIgnoreCase("SetColor") && (!(sender instanceof Player) || sender.isOp())) return setColor(sender, args);
        else if(args[0].equalsIgnoreCase("save") && (!(sender instanceof Player) || sender.isOp())) return saveTeams(sender);
        else if(args[0].equalsIgnoreCase("load") && (!(sender instanceof Player) || sender.isOp())) return loadTeams(sender);
        else if(args[0].equalsIgnoreCase("help") && (!(sender instanceof Player) || sender.isOp())) {
            sender.sendMessage("""
                    Usage:\s
                    /teams add [teamName] [color]
                    /teams remove [teamName]
                    /teams addPlayer [teamName] [player]
                    removePlayer [player]
                    join [teamName]
                    leave
                    setColor [teamName] [color]
                    save
                    load""");
            return true;
        }
        else if(args[0].equalsIgnoreCase("help") && sender instanceof Player) {
            sender.sendMessage("""
                    Usage:\s
                    join [teamName]
                    leave""");
            return true;
        }
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
        ArrayList<String> newArgs = new ArrayList<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].length() > 0 && args[i].charAt(0) == '"') {

                boolean endFound = false;
                String combinedArg = "";

                for(int j = i; j < args.length; j++) {
                    combinedArg+= args[j] + " ";
                    if(args[j].length() >= 2 && args[j].charAt(args[j].length()-1) == '"') {
                        //endpoint has been reached
                        endFound = true;
                        combinedArg = combinedArg.substring(1, combinedArg.length()-2);
                        newArgs.add(combinedArg);
                        i = j;
                        break;
                    }
                }
            } else {
                newArgs.add(args[i]);
            }
        }

        args = newArgs.toArray(new String[0]);

        if(args.length == 1) {
            if (sender instanceof Player p && p.isOp()) {
                ArrayList<String> options = new ArrayList<>(List.of(new String[]{"add", "remove", "addPlayer", "removePlayer", "join", "leave", "setColor", "save", "load"}));
                @NotNull String arg = args[0];
                if(!arg.equals("")) {
                    List<String> filteredOptions = options.stream()
                            .filter(option -> option.startsWith(arg)).toList();
                    System.out.println(filteredOptions);
                    return filteredOptions;
                } else {
                    return options;
                }

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

                ArrayList<String> teamNames = handler.getTeamNames();
                List<String> quotedTeams = new ArrayList<>();
                for (String item : teamNames) {
                    quotedTeams.add("\"" + item + "\"");
                }
                String arg = args[1];
                if(!arg.isEmpty() && arg.charAt(0) != '\"') arg = "\"" + arg;

                if(!arg.equals("\"")) {
                    String finalArg = arg;
                    List<String> filteredTeams = quotedTeams.stream()
                            .filter(option -> option.startsWith(finalArg)).toList();
                    System.out.println(filteredTeams);
                    return filteredTeams;

                } else {
                    return quotedTeams;
                }
            }
            if(args[0].equalsIgnoreCase("removeplayer")) {
                ArrayList<String> playerNames = new ArrayList<>();
                for(Player p:Bukkit.getOnlinePlayers()) {
                    playerNames.add(p.getDisplayName());
                }

                @NotNull String arg = args[1];
                if(!arg.isEmpty()) {
                    List<String> filteredNames = playerNames.stream()
                            .filter(option -> option.startsWith(arg)).toList();
                    return filteredNames;
                } else {
                    return playerNames;
                }
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
    private boolean saveTeams(CommandSender sender) {

        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/teams.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ArrayList<String> teams = new ArrayList<>();
        try {
            for(Team t:TeamHandler.getInstance().getTeams()) {
                teams.add(objectMapper.writeValueAsString(t));
            }
            objectMapper.writeValue(file, teams);
            sender.sendMessage(ChatColor.GREEN + "Teams have been successfully saved");

        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Error Saving Teams");
        }

        return false;
    }

    private boolean loadTeams(CommandSender sender) {
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
            sender.sendMessage(ChatColor.GREEN + "Teams have been successfully loaded");

        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Error Loading Teams");
        }

        return false;
    }


}
