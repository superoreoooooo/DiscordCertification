package org.oreodev.discordcertification.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

import static org.oreodev.discordcertification.Main.chat;

public class DiscordListener extends ListenerAdapter {
    public static HashMap<Player, Integer> codeMap = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Random random = new Random();
        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        User author = event.getAuthor();

        int code = random.nextInt(899999) + 100000;

        if (message.getAuthor().isBot()) return;

        if (message.getContentRaw().contains("/인증")) {
            if (message.getContentRaw().length() <= 4) {
                messageChannel.sendMessage("유저의 이름을 입력해 주세요.").queue();
                return;
            }

            String playerName = message.getContentRaw().substring(message.getContentRaw().lastIndexOf("/")+4);

            if (Bukkit.getPlayer(playerName) == null) {
                messageChannel.sendMessage("존재하지 않는 유저입니다.").queue();
                return;
            }

            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                messageChannel.sendMessage("존재하지 않는 유저입니다.").queue();
                return;
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.WHITE + "인증 요청 발생 : " + playerName);
            message.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("/인증 " + code).queue());

            codeMap.put(player, code);

        }

        if (!chat) return;
        Bukkit.broadcastMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.WHITE + author.getName() + " : " + message.getContentRaw());
    }
}
