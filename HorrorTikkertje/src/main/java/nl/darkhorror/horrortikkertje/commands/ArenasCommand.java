package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenasCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public ArenasCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/arenas list | set <id> | reload");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "list" -> {
                sender.sendMessage("Arenas:");
                for (Arena a : plugin.getArenaManager().getArenas()) sender.sendMessage("- " + a.getId() + " (" + a.getDisplayName() + ")");
            }
            case "set" -> {
                if (args.length < 2) { sender.sendMessage("Gebruik: /arenas set <id>"); return true; }
                Arena arena = plugin.getArenaManager().getArena(args[1]);
                if (arena == null) { sender.sendMessage("Arena niet gevonden."); return true; }
                plugin.getArenaManager().setCurrentArena(arena);
                sender.sendMessage("Arena gezet: " + arena.getId());
            }
            case "reload" -> {
                plugin.getArenaManager().reload();
                sender.sendMessage("Arenas herladen.");
            }
            default -> sender.sendMessage("Onbekende subcommand.");
        }
        return true;
    }
}

