package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Controls game flow and state transitions.
 */
public class GameManager {
    public enum GameState { LOBBY, VOTING, STARTING, RUNNING, ENDING }

    private final HorrorTikkertjePlugin plugin;
    private GameState state = GameState.LOBBY;
    private final Set<Player> players = Collections.synchronizedSet(new HashSet<>());
    private BukkitTask gameTask;

    public GameManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public GameState getState() { return state; }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Set<Player> getPlayers() { return players; }

    public void startGame() {
        if (state == GameState.RUNNING || state == GameState.STARTING) return;
        state = GameState.STARTING;
        // Placeholder: transition immediately to RUNNING
        Bukkit.getScheduler().runTaskLater(plugin, () -> state = GameState.RUNNING, 40L);
    }

    public void endGame() {
        if (state != GameState.RUNNING && state != GameState.STARTING) return;
        state = GameState.ENDING;
        Bukkit.getScheduler().runTaskLater(plugin, () -> state = GameState.LOBBY, 40L);
    }
}

