package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameInfoCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public GameInfoCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("State: " + plugin.getGameManager().getState());
        sender.sendMessage("Players: " + plugin.getGameManager().getPlayers().size());
        return true;
    }
}

