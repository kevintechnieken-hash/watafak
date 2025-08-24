package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import nl.darkhorror.horrortikkertje.util.ColorUtil;

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
        obj.setDisplayName(ColorUtil.colorize("#ff3358&lHAUNT & HUNT"));

        // Clear existing scores by creating new entries with unique lines
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        String ip = plugin.getConfig().getString("server.ip", "mc.azura.gg");
        String arenaName = plugin.getArenaManager().getCurrentArena() != null ? ColorUtil.colorize(plugin.getArenaManager().getCurrentArena().getDisplayName()) : "N/A";
        java.time.LocalDate now = java.time.LocalDate.now();
        String date = now.getYear() + "/" + String.format("%02d", now.getMonthValue()) + "/" + String.format("%02d", now.getDayOfMonth());

        int max = plugin.getConfig().getInt("max-players", 12);
        int waiting = plugin.getGameManager().getPlayers().size();

        int line = 14;
        line = setLine(obj, line, "§7" + date);
        line = setLine(obj, line, blank(1));
        line = setLine(obj, line, "§fPlayers Waiting:");
        line = setLine(obj, line, "§a" + waiting + "/" + max);
        line = setLine(obj, line, blank(2));
        line = setLine(obj, line, "§fMap:");
        line = setLine(obj, line, "§b" + arenaName);
        line = setLine(obj, line, blank(3));
        setLine(obj, line, ColorUtil.colorize("#a6fcff" + ip));
    }

    private int setLine(Objective obj, int line, String text) {
        Score score = obj.getScore(text);
        score.setScore(line);
        return line - 1;
    }

    private String blank(int n) {
        return "§" + (char) ('0' + (n % 10));
    }
}

