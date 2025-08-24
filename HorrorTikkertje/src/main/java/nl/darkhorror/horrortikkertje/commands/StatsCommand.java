package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public StatsCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : (sender instanceof Player ? (Player) sender : null);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }
        UUID uuid = target.getUniqueId();
        int wins = plugin.getStatsManager().getStat(uuid, "wins");
        int kills = plugin.getStatsManager().getStat(uuid, "kills");
        int deaths = plugin.getStatsManager().getStat(uuid, "deaths");
        sender.sendMessage("Stats for " + target.getName() + ": W=" + wins + " K=" + kills + " D=" + deaths);
        return true;
    }
}

