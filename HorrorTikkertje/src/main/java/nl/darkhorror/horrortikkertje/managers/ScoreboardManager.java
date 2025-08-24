package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
                // Placeholder: update scoreboard lines later
            }
        }, 20L, 40L);
    }

    public void stopUpdaterTask() {
        if (updaterTask != null) {
            updaterTask.cancel();
            updaterTask = null;
        }
    }
}

