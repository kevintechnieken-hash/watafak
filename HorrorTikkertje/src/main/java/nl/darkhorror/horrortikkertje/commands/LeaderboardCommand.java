package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LeaderboardCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public LeaderboardCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Leaderboard placeholder");
        return true;
    }
}

