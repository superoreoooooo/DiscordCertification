package org.oreodev.discordcertification;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.oreodev.discordcertification.bot.botConnector;
import org.oreodev.discordcertification.command.CertificationCommand;
import org.oreodev.discordcertification.command.MinecraftCommand;
import org.oreodev.discordcertification.listener.MinecraftListener;
import org.oreodev.discordcertification.manager.ymlManager;

import static org.oreodev.discordcertification.command.CertificationCommand.playerList;
import static org.oreodev.discordcertification.command.CertificationCommand.originList;


import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public ymlManager manager;

    private static Main instance;

    public static boolean chat;
    public static boolean alarm;
    public static boolean status;
    public static String token;
    public static String botStatus;
    public static List<String> discordMsg = new ArrayList<>();

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        instance = this;
        this.manager = new ymlManager(this);
        FileConfiguration config = this.getConfig();
        this.saveDefaultConfig();

        getCommand("jda").setExecutor(new MinecraftCommand(this));
        getCommand("인증").setExecutor(new CertificationCommand(this));

        chat = config.getBoolean("chat");
        alarm = config.getBoolean("alarm");
        status = false;
        botStatus = config.getString("messages.status");
        discordMsg = config.getStringList("messages.discord");

        int cnt = this.manager.getConfig().getInt("count");

        for (int r = 1; r < cnt + 1; r++) {
            String p = config.getString("players." + r);
            if (p == null) {
                console.sendMessage(ChatColor.RED + "error occurred!");
                return;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(p);
            originList.add(player);
            playerList.add(player);
        }

        int ct = 0;
        console.sendMessage("==========================================================");
        console.sendMessage("Certificated Players : ");
        for (OfflinePlayer player : originList) {
            if (player.getName() == null) return;
            console.sendMessage(player.getName());
            ct++;
        }
        console.sendMessage("Total : " + ct);
        console.sendMessage("==========================================================");

        if (config.getString("token") != null) {
            token = config.getString("token");
            Bukkit.getConsoleSender().sendMessage("loaded token : " + ChatColor.AQUA + token);
            status = true;
            pluginManager.registerEvents(new MinecraftListener(this), this);
            try {
                botConnector.startBot(token);
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }
        else {
            status = false;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.no-token-exists")));
        }

        console.sendMessage(ChatColor.GREEN + "JDA BOT ON!");
    }

    @Override
    public void onDisable() {
        int cnt = this.manager.getConfig().getInt("count");

        console.sendMessage("==========================================================");
        console.sendMessage("New certificated players : ");
        for (OfflinePlayer player : playerList) {
            if (!originList.contains(player)) {
                cnt++;
                this.manager.getConfig().set("players." + cnt, player.getName());
                if (player.getName() == null) return;
                this.console.sendMessage(player.getName());
            }
        }
        console.sendMessage("Total : " + cnt);
        console.sendMessage("==========================================================");

        this.manager.getConfig().set("count", playerList.size());
        this.manager.saveConfig();

        if (botConnector.jda == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JDA BOT OFF!");
            return;
        }

        botConnector.jda.shutdownNow();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JDA BOT OFF!");
    }

    public static Main getInstance() {
        return instance;
    }
}
