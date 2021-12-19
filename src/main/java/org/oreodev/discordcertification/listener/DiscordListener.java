package org.oreodev.discordcertification.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.oreodev.discordcertification.Main;

import java.util.HashMap;
import java.util.Random;

import static org.oreodev.discordcertification.Main.chat;

public class DiscordListener extends ListenerAdapter {
    private final Main main;

    public static HashMap<Player, Integer> codeMap = new HashMap<>();

    public DiscordListener(Main main) {
        this.main = main;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        FileConfiguration config = main.getConfig();
        Random random = new Random();
        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        User author = event.getAuthor();

        int code = random.nextInt(899999) + 100000;

        if (message.getAuthor().isBot()) return;

        if (message.getContentRaw().contains("/인증")) {
            if (message.getContentRaw().length() <= 4) {
                messageChannel.sendMessage(config.getString("message.discord-input-username")).queue();
                return;
            }

            String playerName = message.getContentRaw().substring(message.getContentRaw().lastIndexOf("/")+4);

            if (Bukkit.getPlayer(playerName) == null) {
                messageChannel.sendMessage(config.getString("message.discord-user-no-exist")).queue();
                return;
            }

            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                messageChannel.sendMessage(config.getString("message.discord-user-no-exist")).queue();
                return;
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[DISCORD] " + config.getString("message.discord-request-cert")) + playerName);
            message.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("/인증 " + code).queue());

            codeMap.put(player, code);
            return;
        }

        if (!chat) return;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9[DISCORD]&f" + author.getName() + " : " + message.getContentRaw()));
    }
}
