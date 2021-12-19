package org.oreodev.discordcertification.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.oreodev.discordcertification.listener.DiscordListener;

import javax.security.auth.login.LoginException;

public class botConnector {
    public static JDA jda;

    public static void startBot(String token) throws LoginException {
        if (token == null) return;
        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
        .setAutoReconnect(true)
        .setStatus(OnlineStatus.ONLINE)
        .addEventListeners(new DiscordListener())
        .setActivity(Activity.playing("/인증"))
        .build();
    }
}
