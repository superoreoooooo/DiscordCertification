package org.oreodev.discordcertification.command;

import net.dv8tion.jda.api.JDA;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.oreodev.discordcertification.bot.botConnector;

import javax.security.auth.login.LoginException;

import static org.oreodev.discordcertification.Main.status;
import static org.oreodev.discordcertification.Main.token;

public class MinecraftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!status) {
            sender.sendMessage("config.yml에 token 입력한 다음 재로딩 ㄱㄱ");
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Player Only No Console");
            return false;
        }

        if (!sender.hasPermission("administrator")) {
            String message = ChatColor.RED + "권한이 없습니다.";
            sender.sendMessage(message);
            return false;
        }

        if (token == null) {
            String message = ChatColor.RED + "토큰을 입력해주세요.";
            sender.sendMessage(message);
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "/jda " + ChatColor.GOLD + "::shutdown(stop) ::status(now) ::start ::reload(restart)");
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("shutdown") || args[0].equalsIgnoreCase("stop")) {
                sender.sendMessage(ChatColor.GREEN + "jda is now turned off");
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
                    sender.sendMessage(ChatColor.GREEN + "jda is now started");
                    return true;
                }
                else {
                    sender.sendMessage(ChatColor.GREEN + "jda is working now");
                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("restart")) {
                if (botConnector.jda.getStatus().equals(JDA.Status.SHUTDOWN)) {
                    sender.sendMessage(ChatColor.GREEN + "jda is stopped now");
                    return false;
                }
                botConnector.jda.shutdownNow();
                try {
                    botConnector.startBot(token);
                } catch (LoginException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(ChatColor.GREEN + "jda is now restarted");
                return true;
            }
        }

        else {
            sender.sendMessage(ChatColor.GREEN + "/jda" + ChatColor.GOLD + "::shutdown(stop) ::status(now) ::start ::reload(restart)");
        }

        return false;
    }
}
