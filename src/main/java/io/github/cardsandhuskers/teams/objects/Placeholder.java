package io.github.cardsandhuskers.teams.objects;

import io.github.cardsandhuskers.teams.Teams;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static io.github.cardsandhuskers.teams.Teams.handler;
import static io.github.cardsandhuskers.teams.Teams.teamListArray;

public class Placeholder extends PlaceholderExpansion {
    private final Teams plugin;
    ArrayList<Team> teamArrayList;

    public Placeholder(Teams plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getIdentifier() {
        return "Teams";
    }
    @Override
    public String getAuthor() {
        return "cardsandhuskers";
    }
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer p, String s) {

        if(s.equalsIgnoreCase("teamPoints")) {
            if(handler.getPlayerTeam((Player) p)!= null) {
                return "" + handler.getTeamPoints(handler.getPlayerTeam((Player) p));
            } else {
                return "";
            }
        }
        if(s.equalsIgnoreCase("team")) {
            if(handler.getPlayerTeam((Player) p) != null) {
                return handler.getPlayerTeam((Player)p).getTeamName();
            } else {
                return "No Team";
            }

        }
        if(s.equalsIgnoreCase("color")) {
            if(handler.getPlayerTeam((Player) p) != null) {
                return handler.getPlayerTeam((Player)p).getConfigColor();
            } else {
                return "&f";
            }
        }

        //Get lines, so check
        // if ahead != 1st place use 4 lines, 1st, ahead, team, behind
        //if it is equal, just use 3 lines, ahead, team, behind
        //if player is on 1st place team, then team, behind, behind
        //if player is on last place team, first, ahead, ahead, team

        //always has first place
        if(s.equalsIgnoreCase("teamLine1")) {
            Team firstTeam = getFirstPlace();
            if(firstTeam != null) {
                return String.format("%-20s %5s", getDistanceFromEnd(firstTeam) + ". " + firstTeam.getConfigColor() + "&l" + firstTeam.getTeamName() +  "&r", firstTeam.getPoints());
                //return String.format("%-30s", "THIS IS A TITLE", "5000");

            } else {
                return "TEAM";
            }
        }


        teamArrayList = handler.getPointsSortedList();
        int pos = teamArrayList.indexOf(handler.getPlayerTeam((Player)p));
        Team playerTeam = handler.getPlayerTeam((Player) p);
        int numTeams = teamArrayList.size();
        //if team 1st, line equals behind
        //if team 2nd, line equals team
        //if team 3-n-1 line equals ahead
        //if team n line equals ahead.ahead
        if(s.equalsIgnoreCase("teamLine2")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == numTeams-1) {
                //first place
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return String.format("%-20s %5s", getDistanceFromEnd(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "&r", behind.getPoints());
                    //return String.format("%-30s", "T", "5000");
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams - 2) {
                //second place
                if(playerTeam != null) {
                    return getDistanceFromEnd(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + "    &r" + playerTeam.getPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == 0) {
                //last place
                Team ahead = getAhead(playerTeam);
                Team doubleAhead;
                if(ahead != null) {
                    doubleAhead = getAhead(ahead);
                } else {
                    return "TEAM";
                }

                if(doubleAhead != null) {
                    return getDistanceFromEnd(doubleAhead) + ". " + doubleAhead.getConfigColor() + "&l" + doubleAhead.getTeamName() + "     &r" + doubleAhead.getPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                Team ahead = getAhead(playerTeam);
                if(ahead != null) {
                    return getDistanceFromEnd(ahead) + ". " + ahead.getConfigColor() + "&l" + ahead.getTeamName() + "   &r" + ahead.getPoints();
                } else {
                    return "TEAM";
                }
            }
        }
        if(s.equalsIgnoreCase("teamLine3")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == numTeams-1) {
                //first place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(getBehind(playerTeam));
                } else {
                    return "TEAM";
                }
                if(doubleBehind != null) {
                    return getDistanceFromEnd(doubleBehind) + ". " + doubleBehind.getConfigColor() + "&l" + doubleBehind.getTeamName() + "    &r" + doubleBehind.getPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams - 2) {
                //second place
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return getDistanceFromEnd(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "    &r" + behind.getPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == 0) {
                //last place
                Team ahead = getAhead(playerTeam);
                if(ahead != null) {
                    return getDistanceFromEnd(ahead) + ". " + ahead.getConfigColor() + "&l" + ahead.getTeamName() + "     &r" + ahead.getPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                if(playerTeam != null) {
                    return getDistanceFromEnd(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + "   &r" + playerTeam.getPoints();
                } else {
                    return "TEAM";
                }
            }
        }
        if(s.equalsIgnoreCase("teamLine4")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == numTeams-1) {
                //first place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                Team tripleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(behind);
                    if(doubleBehind!= null) {
                        tripleBehind = getBehind(doubleBehind);
                    } else {
                        return "TEAM";
                    }
                } else {
                    return "TEAM";
                }
                if(tripleBehind != null) {
                    return getDistanceFromEnd(tripleBehind) + ". " + tripleBehind.getConfigColor() + "&l" + tripleBehind.getTeamName() + "    &r" + tripleBehind.getPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams - 2) {
                //second place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(behind);
                } else {
                    return "TEAM";
                }

                if(doubleBehind != null) {
                    return getDistanceFromEnd(doubleBehind) + ". " + doubleBehind.getConfigColor() + "&l" + doubleBehind.getTeamName() + "    &r" + doubleBehind.getPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == 0) {
                //last place
                if(playerTeam != null) {
                    return getDistanceFromEnd(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + "     &r" + playerTeam.getPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return getDistanceFromEnd(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "   &r" + behind.getPoints();
                } else {
                    return "TEAM";
                }
            }
        }
        return null;
    }


    public Team getAhead(Team t) {
        ArrayList<Team> teamArrayList = handler.getPointsSortedList();
        int teamIndex = teamArrayList.indexOf(t);
        if(teamIndex < teamArrayList.size()-1) {
            return teamArrayList.get(teamIndex + 1);
        } else {
            return null;
        }
    }

    public Team getBehind(Team t) {
        ArrayList<Team> teamArrayList = handler.getPointsSortedList();
        int teamIndex = teamArrayList.indexOf(t);
        if(teamArrayList.size() > 0) {
            if(teamIndex > 0) {
                return teamArrayList.get(teamIndex - 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public Team getFirstPlace() {
        ArrayList<Team> teamArrayList = handler.getPointsSortedList();
        if(teamArrayList.size()>0) {
            Team team = teamArrayList.get(teamArrayList.size() - 1);
            return team;
        } else {
            return null;
        }

    }
    public int getPosition(Team t) {
        try {
            ArrayList<Team> teamArrayList = handler.getPointsSortedList();
            int index = teamArrayList.indexOf(t);
            return index;
        } catch (Exception e) {
            return 0;
        }
    }
    public int getDistanceFromEnd(Team t) {
        return teamArrayList.size() - teamArrayList.indexOf(t);
    }
}
