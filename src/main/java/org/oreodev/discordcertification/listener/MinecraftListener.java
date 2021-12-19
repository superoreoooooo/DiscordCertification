package org.oreodev.discordcertification.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.oreodev.discordcertification.Main;
import org.oreodev.discordcertification.bot.botConnector;

import java.util.HashMap;

import static org.oreodev.discordcertification.command.CertificationCommand.playerList;
import static org.oreodev.discordcertification.Main.chat;
import static org.oreodev.discordcertification.Main.alarm;

public class MinecraftListener implements Listener {

    private Main main;

    public static HashMap<Player, Location> LocationMap = new HashMap<>();

    public MinecraftListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!chat) return;
        Player player = e.getPlayer();
        String chat = e.getMessage();
        FileConfiguration config = main.getConfig();
        String channelName = config.getString("channelname");
        botConnector.jda.getTextChannelsByName(channelName, false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + main.getConfig().getString("messages.mc-event-chat") + chat).queue());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration config = main.getConfig();
        Player player = e.getPlayer();
        msg(player, config.getString("messages.mc-welcome"));
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

        msg(player, config.getString("messages.mc-requesting-cert"));
        LocationMap.put(player, player.getLocation());
        player.teleport(new Location(player.getWorld(), 140.5, 5.0, -168.5));

        if (!alarm) return;
        String channelName = config.getString("channelname");
        botConnector.jda.getTextChannelsByName(channelName, false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + config.getString("messages.mc-event-join")).queue());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        FileConfiguration config = main.getConfig();
        if (!alarm) return;
        Player player = e.getPlayer();
        String channelName = config.getString("channelname");
        botConnector.jda.getTextChannelsByName(channelName, false).forEach(TextChannel -> TextChannel.sendMessage(player.getName() + config.getString("messages.mc-event-quit")).queue());
    }

    private void msg(Player player, String msg) {
        FileConfiguration config = main.getConfig();
        String prefix = config.getString("messages.prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }
}
