package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.BungeeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

public class AdminCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public AdminCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/admin kick <player> [reason] | ban <player> [reason] | tp <player> <target> | resetstats <player> | send <player|all> <server> | reload");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "kick" -> {
                if (args.length < 2) { sender.sendMessage("Usage: /admin kick <player> [reason]"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { sender.sendMessage("Player not online."); return true; }
                String reason = args.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "Kicked by admin";
                t.kickPlayer(org.bukkit.ChatColor.RED + reason);
            }
            case "ban" -> {
                if (args.length < 2) { sender.sendMessage("Usage: /admin ban <player> [reason]"); return true; }
                String name = args[1];
                String reason = args.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "Banned by admin";
                Bukkit.getBanList(BanList.Type.NAME).addBan(name, reason, null, sender.getName());
                Player t = Bukkit.getPlayer(name);
                if (t != null) t.kickPlayer(org.bukkit.ChatColor.RED + reason);
            }
            case "tp" -> {
                if (args.length < 3) { sender.sendMessage("Usage: /admin tp <player> <target>"); return true; }
                Player a = Bukkit.getPlayer(args[1]);
                Player b = Bukkit.getPlayer(args[2]);
                if (a == null || b == null) { sender.sendMessage("Player not found."); return true; }
                a.teleport(b.getLocation());
            }
            case "resetstats" -> {
                if (args.length < 2) { sender.sendMessage("Usage: /admin resetstats <player>"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { sender.sendMessage("Player not online."); return true; }
                plugin.getStatsManager().resetStats(t.getUniqueId());
                sender.sendMessage("Stats reset.");
            }
            case "send" -> {
                if (!(sender instanceof Player p)) { sender.sendMessage("Players only"); return true; }
                if (args.length < 3) { sender.sendMessage("Usage: /admin send <player|all> <server>"); return true; }
                String target = args[1];
                String server = args[2];
                if (target.equalsIgnoreCase("all")) {
                    for (Player pl : Bukkit.getOnlinePlayers()) BungeeUtil.connect(pl, server, plugin);
                } else {
                    Player pl = Bukkit.getPlayer(target);
                    if (pl == null) { sender.sendMessage("Player not online."); return true; }
                    BungeeUtil.connect(pl, server, plugin);
                }
                sender.sendMessage("Sent to server " + server);
            }
            case "reload" -> {
                plugin.getConfigManager().reloadAll();
                sender.sendMessage("Configs reloaded.");
            }
            default -> sender.sendMessage("Unknown subcommand.");
        }
        return true;
    }
}

