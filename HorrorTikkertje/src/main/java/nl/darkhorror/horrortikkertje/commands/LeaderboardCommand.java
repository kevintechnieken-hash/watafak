package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public LeaderboardCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String column = args.length > 0 ? args[0] : "wins";
        if (sender instanceof Player p) {
            plugin.getGuiManager().openLeaderboard(p, column);
        } else {
            var lines = plugin.getStatsManager().getLeaderboard(column, 10);
            for (String line : lines) sender.sendMessage(line);
        }
        return true;
    }
}

