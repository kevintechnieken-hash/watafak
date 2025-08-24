package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameConfigCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public GameConfigCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getConfigManager().reloadAll();
        sender.sendMessage("Configs reloaded.");
        return true;
    }
}

