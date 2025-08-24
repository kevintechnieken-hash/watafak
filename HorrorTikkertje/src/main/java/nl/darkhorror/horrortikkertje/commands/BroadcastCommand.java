package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public BroadcastCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /broadcast <message>");
            return true;
        }
        String msg = String.join(" ", args);
        plugin.getBroadcastManager().broadcast("&a[Broadcast] &f" + msg);
        return true;
    }
}

