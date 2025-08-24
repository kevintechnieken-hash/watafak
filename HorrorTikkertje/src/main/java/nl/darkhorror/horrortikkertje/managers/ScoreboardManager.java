package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Handles creation and periodic updates of player scoreboards.
 */
public class ScoreboardManager {
    private final HorrorTikkertjePlugin plugin;
    private BukkitTask updaterTask;

    public ScoreboardManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public void startUpdaterTask() {
        if (updaterTask != null) return;
        updaterTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, 20L, 40L);
    }

    public void stopUpdaterTask() {
        if (updaterTask != null) {
            updaterTask.cancel();
            updaterTask = null;
        }
    }

    private void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        if (board == null || board == Bukkit.getScoreboardManager().getMainScoreboard()) {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(board);
        }

        Objective obj = board.getObjective("ht");
        if (obj == null) {
            obj = board.registerNewObjective("ht", "dummy", "Horror Tikkertje");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        obj.setDisplayName("§c§lHAUNT & HUNT");

        // Clear existing scores by creating new entries with unique lines
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        String ip = plugin.getConfig().getString("server.ip", "play.example.net");
        String arenaName = plugin.getArenaManager().getCurrentArena() != null ? plugin.getArenaManager().getCurrentArena().getDisplayName() : "N/A";
        String region = plugin.getRegionManager().toDisplay(plugin.getRegionManager().getCurrentRegion());

        int line = 14;
        line = setLine(obj, line, "§8────────────");
        line = setLine(obj, line, "§7State: §f" + plugin.getGameManager().getState());
        line = setLine(obj, line, "§7Players: §f" + plugin.getGameManager().getPlayers().size());
        line = setLine(obj, line, "§7Arena: §f" + arenaName);
        line = setLine(obj, line, "§7Region: §f" + region);
        line = setLine(obj, line, "§7Time: §f" + plugin.getGameManager().getCountdown() + "s");
        line = setLine(obj, line, "§7Monster: " + (plugin.getVoteManager().isEnabled(VoteManager.Option.MONSTER_ENABLED) ? "§aON" : "§cOFF"));
        line = setLine(obj, line, "§7Cursed: " + (plugin.getVoteManager().isEnabled(VoteManager.Option.CURSED_ARENA) ? "§aON" : "§cOFF"));
        line = setLine(obj, line, " ");
        line = setLine(obj, line, "§7IP: §f" + ip);
        setLine(obj, line, "§8────────────");
    }

    private int setLine(Objective obj, int line, String text) {
        Score score = obj.getScore(text);
        score.setScore(line);
        return line - 1;
    }
}

