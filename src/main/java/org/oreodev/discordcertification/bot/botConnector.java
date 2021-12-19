package org.oreodev.discordcertification.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;
import org.oreodev.discordcertification.Main;
import org.oreodev.discordcertification.listener.DiscordListener;

import javax.security.auth.login.LoginException;

import static org.oreodev.discordcertification.Main.botStatus;

public class botConnector {
    private static final Main plugin = Main.getInstance();
    public static JDA jda;

    public static void startBot(String token) throws LoginException {
        if (token == null) return;
        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
        .setAutoReconnect(true)
        .setStatus(OnlineStatus.ONLINE)
        .addEventListeners(new DiscordListener(plugin))
        .setActivity(Activity.playing("/" + botStatus))
        .build();
    }
}
