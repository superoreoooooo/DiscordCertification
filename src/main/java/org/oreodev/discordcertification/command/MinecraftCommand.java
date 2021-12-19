package org.oreodev.discordcertification.command;

import net.dv8tion.jda.api.JDA;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.oreodev.discordcertification.Main;
import org.oreodev.discordcertification.bot.botConnector;

import javax.security.auth.login.LoginException;

import static org.oreodev.discordcertification.Main.token;

public class MinecraftCommand implements CommandExecutor {

    private final Main main;

    public MinecraftCommand(Main plugin) {
        this.main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = main.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage("Player Only No Console");
            return false;
        }

        if (!sender.hasPermission("administrator")) {
            msg(sender, config.getString("messages.no-permission"));
            return false;
        }

        if (token == null) {
            msg(sender, config.getString("messages.no-token-exists"));
            return false;
        }

        if (args.length == 0) {
            msg(sender, config.getString("messages.wrong-input2"));
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("shutdown") || args[0].equalsIgnoreCase("stop")) {
                msg(sender, config.getString("messages.jda-stop"));
                botConnector.jda.shutdownNow();
                return true;
            }

            if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("now")) {
                sender.sendMessage(ChatColor.GREEN + botConnector.jda.getStatus().toString());
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {
                if (botConnector.jda.getStatus().equals(JDA.Status.SHUTDOWN)) {
                    try {
                        botConnector.startBot(token);
                    } catch (LoginException e) {
                        e.printStackTrace();
                    }
                    msg(sender, config.getString("messages.jda-start"));
                    return true;
                }
                else {
                    msg(sender, config.getString("messages.jda-working"));
                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("restart")) {
                if (botConnector.jda.getStatus().equals(JDA.Status.SHUTDOWN)) {
                    msg(sender, config.getString("messages.jda-stop"));
                    return false;
                }
                botConnector.jda.shutdownNow();
                try {
                    botConnector.startBot(token);
                } catch (LoginException e) {
                    e.printStackTrace();
                }
                msg(sender, config.getString("messages.jda-restart"));
                return true;
            }
        }

        else {
            msg(sender, config.getString("messages.wrong-input2"));
        }

        return false;
    }

    private void msg(CommandSender sender, String msg) {
        FileConfiguration config = main.getConfig();
        String prefix = config.getString("messages.prefix");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }
}
