package io.github.cardsandhuskers.teams.handlers;

import com.mojang.authlib.GameProfile;
import io.github.cardsandhuskers.teams.objects.Team;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

import static io.github.cardsandhuskers.teams.Teams.*;

public class TablistHandler {


    public TablistHandler() {

    }



    public void buildTablist() {
        /*
        TabAPI tabAPI = TabAPI.getInstance();
        TeamManager teamManager = tabAPI.getTeamManager();
        HeaderFooterManager headerManager = tabAPI.getHeaderFooterManager();

         */

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for(org.bukkit.scoreboard.Team t:scoreboard.getTeams()) {
            t.unregister();
        }

        for(Team t: handler.getTeams()) {
            org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam(t.getTeamName());
            team.setColor(t.getChatColor());
            team.setAllowFriendlyFire(false);
            for(Player p:t.getOnlinePlayers()) {
                team.addEntry(p.getDisplayName());
                team.setPrefix("[" + t.getTeamName().substring(0,1) + "] ");
            }
        }

    }



    public void buildTabListOLD(Player player) {

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel(); //level = world


        //Remove Fake Players
        for (ServerPlayer fakeP : teamListArray) {
            for(Player p:Bukkit.getOnlinePlayers()) {

                CraftPlayer craftP = (CraftPlayer) p;
                ServerPlayer sp = craftP.getHandle();

                ServerGamePacketListenerImpl packetListener = sp.connection;
                packetListener.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, fakeP));
            }
        }
        teamListArray.clear();


    }
}
