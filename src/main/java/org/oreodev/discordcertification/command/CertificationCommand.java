package org.oreodev.discordcertification.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.oreodev.discordcertification.Main;

import java.util.ArrayList;
import java.util.List;

import static org.oreodev.discordcertification.listener.DiscordListener.codeMap;
import static org.oreodev.discordcertification.listener.MinecraftListener.LocationMap;
import static org.oreodev.discordcertification.Main.status;

public class CertificationCommand implements CommandExecutor {

    private final Main main;
    public static List<OfflinePlayer> playerList = new ArrayList<>();
    public static List<OfflinePlayer> originList = new ArrayList<>();

    public CertificationCommand(Main plugin) {
        this.main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = main.getConfig();

        if (!status) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.prefix") + config.getString("messages.no-token-exists")));
        }

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!codeMap.containsKey(player)) {
            msg(player, config.getString("messages.no-certification-requests"));
            return false;
        }

        int code = codeMap.get(player);
        Bukkit.getConsoleSender().sendMessage(player.getName() + "'s code : " + codeMap.get(player));
        if (args.length == 1) {
            if (args[0].equals(String.valueOf(code))) {
                msg(player, config.getString("messages.certificated"));

                playerList.add(player);
                player.teleport(LocationMap.get(player));

                main.manager.getConfig().set("count", main.manager.getConfig().getInt("count") + 1);
                Bukkit.getConsoleSender().sendMessage("Player " + player.getName() + " certificated / " + "count : " + ChatColor.AQUA + main.manager.getConfig().getInt("count") + 1);
                main.manager.saveConfig();
            }
            else {
                msg(player, config.getString("messages.certification-failed"));
            }
            codeMap.remove(player);
            return true;
        }
        else {
            msg(player, config.getString("messages.wrong-input"));
        }
        return false;
    }

    private void msg(Player player, String msg) {
        FileConfiguration config = main.getConfig();
        String prefix = config.getString("messages.prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }
}
