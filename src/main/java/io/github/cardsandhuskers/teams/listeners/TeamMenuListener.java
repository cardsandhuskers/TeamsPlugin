package io.github.cardsandhuskers.teams.listeners;

import io.github.cardsandhuskers.teams.Teams;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;
import io.github.cardsandhuskers.teams.objects.Menu;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.cardsandhuskers.teams.Teams.playerMenus;
import static io.github.cardsandhuskers.teams.Teams.openColorInvs;


public class TeamMenuListener implements Listener {
    TeamHandler handler = Teams.handler;

    Plugin plugin;


    public TeamMenuListener(Plugin p) {
        plugin = p;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        String invName = e.getView().getTitle();
        //System.out.println(invName);

        if (ChatColor.stripColor(invName).equalsIgnoreCase("Team Menu")) {
            //System.out.println("TEST");
            HumanEntity p = e.getWhoClicked();
            Player player = (Player) p;
            if (e.getCurrentItem() == null) {
                return;
            }

            if (isValid(e.getCurrentItem().getType())) {
                String s = e.getCurrentItem().getItemMeta().getDisplayName();
                s = ChatColor.stripColor(s);
                boolean result = handler.addPlayer(player, s);
                if (result == false) {
                    p.sendMessage(ChatColor.RED + "ERROR: You are already on that team");
                }
            }

            if (e.getCurrentItem().getType() == Material.NAME_TAG) {
                //System.out.println("TEST2");
                AtomicBoolean result = new AtomicBoolean(false);
                new AnvilGUI.Builder()
                        .onClose(player1 -> {                                               //called when the inventory is closing
                            if(result.get()) {
                                openColorSelector(player, true);
                            }

                        })
                        .onClick((slot, stateSnapshot) -> {                                    //called when the inventory output slot is clicked
                            String teamName = stateSnapshot.getText().trim();

                            if(teamName.length() <= 20 && !(teamName.equals(""))) {
                                result.set(handler.createTeam(teamName));
                            } else {
                                player.sendMessage("Team Name must be between 1 and 20 Characters.");
                                result.set(false);
                            }


                            if (result.get() == true) {
                                handler.addPlayer(player, teamName);

                            } else {
                                player.sendMessage("Could not Create Team");
                            }
                            return AnvilGUI.Response.close();
                        })
                        .text(" ")                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.PAPER))                      //use a custom item for the first slot
                        .title("Enter Team Name:")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(plugin)                                          //set the plugin instance
                        .open(player);                                                   //opens the GUI for the player provided
            }

            if (e.getCurrentItem().getType() == Material.BARRIER) {
                if (!(handler.getPlayerTeam(player) == null)) {
                    handler.removePlayer(player, handler.getPlayerTeam(player));
                    if(openColorInvs != null) {
                        for(Player player2:openColorInvs) {
                            openColorSelector(player2, false);
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "ERROR: You are not on a team");
                }
            }

            if (e.getSlot() == 24) {
                if (handler.getPlayerTeam(player) == null) {

                } else {
                    handler.getPlayerTeam(player).toggleReady();
                }
            }

            for (Menu m : playerMenus) {
                m.populateTeams();
            }
            e.setCancelled(true);
        }
        if (ChatColor.stripColor(invName).equalsIgnoreCase("Color Selection")) {
            if(e.getCurrentItem() != null && isValid(e.getCurrentItem().getType())) {
                Player p = (Player) e.getWhoClicked();
                handler.setColor(handler.getPlayerTeam(p), getColorString(e.getCurrentItem().getType()));

                p.sendMessage(handler.getPlayerTeam(p).color + "Team " + handler.getPlayerTeam(p).getTeamName() + " created successfully!");
                openColorInvs.remove(p);
                p.closeInventory();
                if(openColorInvs != null) {
                    for(Player player:openColorInvs) {
                        openColorSelector(player, false);
                    }
                }
            }
        }
    }

    /**
     * Opens/updates the color picker inventory for the specified player
     * @param p player to open inventory on
     * @param add add player to list or not
     */
    private void openColorSelector(Player p, boolean add) {
        Inventory colorInv = Bukkit.createInventory(p, 18, ChatColor.LIGHT_PURPLE + "Color Selection");
        int counter = 0;
        for(String s:handler.getColors()) {
            ItemStack wool = new ItemStack(getWoolColor(s), 1);
            colorInv.setItem(counter, wool);
            counter++;
        }
        if(handler.getPlayerTeam(p) != null) {
            ItemStack wool = new ItemStack(getWoolColor(handler.getPlayerTeam(p).color), 1);
            colorInv.setItem(counter, wool);
        }

        p.openInventory(colorInv);
        if(add) {
            openColorInvs.add(p);
            if(openColorInvs != null) {
                for(Player p2:openColorInvs) {
                    openColorSelector(p2, false);
                }
            }
        }
    }

    public boolean isValid(Material m) {
        switch (m) {
            case GREEN_WOOL:
            case LIGHT_BLUE_WOOL:
            case CYAN_WOOL:
            case PURPLE_WOOL:
            case ORANGE_WOOL:
            case LIGHT_GRAY_WOOL:
            case GRAY_WOOL:
            case BLUE_WOOL:
            case LIME_WOOL:
            case RED_WOOL:
            case PINK_WOOL:
            case YELLOW_WOOL: return true;
            default: return false;
        }
    }

    public String getColorString(Material m) {
        switch (m) {
            case GREEN_WOOL: return "§2";
            case CYAN_WOOL: return "§3";
            case PURPLE_WOOL: return "§5";
            case ORANGE_WOOL: return "§6";
            case LIGHT_GRAY_WOOL: return "§7";
            case GRAY_WOOL: return "§8";
            case BLUE_WOOL: return "§9";
            case LIME_WOOL: return "§a";
            case LIGHT_BLUE_WOOL: return "§b";
            case RED_WOOL: return "§c";
            case PINK_WOOL: return "§d";
            case YELLOW_WOOL: return "§e";
            default: return "§f";
        }
    }

    /**
     * returns the wool material representing the team's color
     * @return Material
     */
    public Material getWoolColor(String color) {
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


}
