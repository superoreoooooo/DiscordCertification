package org.oreodev.discordcertification.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        if (!status) {
            sender.sendMessage("config.yml에 token 입력한 다음 재로딩 ㄱㄱ.");
        }
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!codeMap.containsKey(player)) {
            player.sendMessage("인증 요청을 먼저 보내주세요.");
            return false;
        }
        int code = codeMap.get(player);
        Bukkit.getConsoleSender().sendMessage(player.getName() + "님의 인증 코드 : " + codeMap.get(player));
        if (args.length == 1) {
            if (args[0].equals(String.valueOf(code))) {
                player.sendMessage("인증되었습니다.");
                codeMap.remove(player);
                player.teleport(LocationMap.get(player));
                playerList.add(player);

                int cnt = main.manager.getConfig().getInt("count");
                main.manager.getConfig().set("count", cnt++);
                Bukkit.getConsoleSender().sendMessage(player.getName() + "님이 인증됨 / " + "count : " + ChatColor.AQUA + cnt);
                main.manager.saveConfig();
            }
            else {
                player.sendMessage("인증 코드가 일치하지 않습니다. 인증을 취소합니다.");
                codeMap.remove(player);
            }
            return true;
        }
        else {
            player.sendMessage("?");
        }
        return false;
    }
}
