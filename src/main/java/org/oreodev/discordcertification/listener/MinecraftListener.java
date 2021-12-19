package org.oreodev.discordcertification.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.oreodev.discordcertification.bot.botConnector;

import java.util.HashMap;

import static org.oreodev.discordcertification.command.CertificationCommand.playerList;
import static org.oreodev.discordcertification.Main.chat;
import static org.oreodev.discordcertification.Main.alarm;

public class MinecraftListener implements Listener {

    public static HashMap<Player, Location> LocationMap = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!chat) return;
        Player player = e.getPlayer();
        String chat = e.getMessage();
        botConnector.jda.getTextChannelsByName("테스트", false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + "이(가) 보낸 메세지 : " + chat).queue());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        player.sendMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.WHITE + "환영합니다.");
        for (OfflinePlayer p : playerList) {
            if (p.getName() == null) return;
            Bukkit.getConsoleSender().sendMessage(p.getName());
            if (player == p) {
                return;
            }
        }
        if (playerList.contains(player)) {
            return;
        }

        player.sendMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.WHITE + "인증을 해주세요.");
        LocationMap.put(player, player.getLocation());
        player.teleport(new Location(player.getWorld(), 140.5, 5.0, -168.5));

        if (!alarm) return;
        botConnector.jda.getTextChannelsByName("테스트", false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + "님이 서버에 접속하셨습니다.").queue());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!alarm) return;
        Player player = e.getPlayer();
        botConnector.jda.getTextChannelsByName("테스트", false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + "님이 서버에서 퇴장하셨습니다.").queue());
    }

    @EventHandler
    public void onDie(PlayerDeathEvent e) {
        if (!alarm) return;
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        botConnector.jda.getTextChannelsByName("테스트", false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + " 님이 " + killer.getName() + " 님에 의해 사망하였습니다.").queue());
    }
}
