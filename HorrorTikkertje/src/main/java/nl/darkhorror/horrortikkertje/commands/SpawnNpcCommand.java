package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnNpcCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public SpawnNpcCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }
        String name = args.length > 0 ? String.join(" ", args) : "#AAAAAAGhost";
        plugin.getNpcManager().spawnGhost(player.getLocation(), name);
        sender.sendMessage("Spawned ghost NPC.");
        return true;
    }
}

