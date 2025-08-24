package nl.darkhorror.horrortikkertje.commands;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MonsterCommand implements CommandExecutor {
    private final HorrorTikkertjePlugin plugin;
    public MonsterCommand(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/monster spawn | remove | speed <value> | health <value>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "spawn" -> {
                var arena = plugin.getArenaManager().getCurrentArena();
                plugin.getMonsterManager().spawnPreGame(arena);
                sender.sendMessage("Monster spawned.");
            }
            case "remove" -> {
                plugin.getMonsterManager().removeMonster();
                sender.sendMessage("Monster removed.");
            }
            case "speed" -> {
                if (args.length < 2) { sender.sendMessage("Usage: /monster speed <value>"); return true; }
                double v = Double.parseDouble(args[1]);
                plugin.getMonsterManager().setSpeed(v);
                sender.sendMessage("Monster speed set to " + v);
            }
            case "health" -> {
                if (args.length < 2) { sender.sendMessage("Usage: /monster health <value>"); return true; }
                double v = Double.parseDouble(args[1]);
                plugin.getMonsterManager().setMaxHealth(v);
                sender.sendMessage("Monster HP set to " + v);
            }
            default -> sender.sendMessage("Unknown subcommand.");
        }
        return true;
    }
}

