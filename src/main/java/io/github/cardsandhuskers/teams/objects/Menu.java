package io.github.cardsandhuskers.teams.objects;

import io.github.cardsandhuskers.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.cardsandhuskers.teams.handlers.TeamHandler;

import java.util.ArrayList;

public class Menu {
    public TeamHandler handler = Teams.handler;
    public Inventory inv;
    public Player player;


    public Menu(Player p) {
        player = p;

    }
    public void generateMenu(Player p) {
        inv = Bukkit.createInventory(p, 27, ChatColor.AQUA + "Team Menu");

        //Page Changers
        /*
        ItemStack lastPage = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta lastPageMeta = lastPage.getItemMeta();
        lastPageMeta.setDisplayName(ChatColor.AQUA + "Previous Page");
        lastPage.setItemMeta(lastPageMeta);

        ItemStack nextPage = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta nextPageMeta = lastPage.getItemMeta();
        nextPageMeta.setDisplayName(ChatColor.AQUA + "Next Page");
        nextPage.setItemMeta(nextPageMeta);
        */

        populateTeams();

        //Create and Leave team
        ItemStack createTeam = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta createTeamMeta = createTeam.getItemMeta();
        createTeamMeta.setDisplayName(ChatColor.WHITE + "Create Team");
        createTeam.setItemMeta(createTeamMeta);

        ItemStack leaveTeam = new ItemStack(Material.BARRIER, 1);
        ItemMeta leaveTeamMeta = createTeam.getItemMeta();
        leaveTeamMeta.setDisplayName(ChatColor.WHITE + "Leave Team");
        leaveTeam.setItemMeta(leaveTeamMeta);

        ItemStack readyItem = new ItemStack(Material.RED_BANNER, 1);
        ItemMeta readyItemMeta = readyItem.getItemMeta();
        readyItemMeta.setDisplayName(ChatColor.WHITE + "Ready Up");
        readyItem.setItemMeta(readyItemMeta);


        inv.setItem(20, createTeam);
        inv.setItem(22, leaveTeam);
        inv.setItem(24, readyItem);


        p.openInventory(inv);
    }

    public void populateTeams() {


        //empties out the teams portion of the menu
        for(int i = 0; i < 18; i++) {
            inv.setItem(i, null);
        }
        //checks if any teams exist (prevents null pointer)
        if(handler.getNumTeams() == 0) {

        } else {
            //for each team add the item to the menu
            int max;
            if(handler.getNumTeams() <= 18) {
                max = handler.getNumTeams();
            } else {
                max = 18;
            }

            ArrayList<ItemStack> teamStack = new ArrayList<>();

            for(int i = 0; i < max; i++) {
                //if team is empty, just add 1 (This should not happen, exists as a fallback)
                if(handler.getTeam(i).getPlayers().isEmpty()) {
                    teamStack.add(new ItemStack(handler.getTeam(i).getWoolColor(), 1));
                } else {
                    teamStack.add(new ItemStack(handler.getTeam(i).getWoolColor(), handler.getTeam(i).getSize()));
                }

                ItemMeta teamMeta = teamStack.get(i).getItemMeta();
                teamMeta.setDisplayName(handler.getTeam(i).color + handler.getTeam(i).getTeamName());

                if(handler.getTeam(i).isReady()) {
                    teamStack.get(i).addUnsafeEnchantment(Enchantment.LURE, 1);
                    teamMeta = teamStack.get(i).getItemMeta();
                    teamMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }


                ArrayList<String> teamLore = new ArrayList<>();
                teamLore.clear();
                //sets lore to empty for empty team (should not ever happen)
                if(handler.getTeam(i).getPlayers().isEmpty()) {
                    teamLore.add("EMPTY");
                } else {
                    //loops through members of the team to add to the lore
                    for(int j = 0; j < handler.getTeam(i).getSize(); j++) {
                        teamLore.add(ChatColor.WHITE + handler.getTeam(i).getPlayer(j).getName());
                    }
                }

                teamMeta.setLore(teamLore);
                teamStack.get(i).setItemMeta(teamMeta);
                inv.setItem(i, teamStack.get(i));

            }
        }

    }

}
