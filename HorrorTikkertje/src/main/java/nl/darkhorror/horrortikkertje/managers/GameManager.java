package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
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
    private int countdown;
    private final java.util.Map<java.util.UUID, Integer> gameKills = new java.util.HashMap<>();

    public GameManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public GameState getState() { return state; }

    public void addPlayer(Player player) {
        players.add(player);
        gameKills.putIfAbsent(player.getUniqueId(), 0);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        gameKills.remove(player.getUniqueId());
    }

    public Set<Player> getPlayers() { return players; }

    public void startGame() {
        if (state == GameState.RUNNING || state == GameState.STARTING) return;
        Arena arena = plugin.getArenaManager().getCurrentArena();
        if (arena == null) return;

        // Voting phase
        state = GameState.VOTING;
        plugin.getVoteManager().clear();
        broadcast("Voting started. Use /vote");
        countdown = plugin.getConfig().getInt("timers.vote", 30);
        runCountdown(() -> startPreStart(arena));
    }

    public void endGame() {
        if (state != GameState.RUNNING && state != GameState.STARTING) return;
        state = GameState.ENDING;
        countdown = plugin.getConfig().getInt("timers.end", 10);
        runCountdown(() -> {
            state = GameState.LOBBY;
            broadcast("Game ended. Returning to lobby.");
        });
    }

    private void startPreStart(Arena arena) {
        state = GameState.STARTING;
        broadcast("Pre-start countdown.");
        // Remove lobby items to prevent retaining kit/vote
        for (Player p : players) {
            p.getInventory().remove(org.bukkit.Material.BOOK);
            p.getInventory().remove(org.bukkit.Material.CHEST);
            p.getInventory().remove(org.bukkit.Material.COMPASS);
        }
        // Spawn monster early if enabled
        if (plugin.getVoteManager().isEnabled(VoteManager.Option.MONSTER_ENABLED)) {
            plugin.getMonsterManager().spawnPreGame(arena);
        }
        countdown = plugin.getConfig().getInt("timers.prestart", 15);
        runCountdown(() -> startRunning(arena));
    }

    private void startRunning(Arena arena) {
        state = GameState.RUNNING;
        // Teleport players and apply kits
        for (Player p : players) {
            if (arena.getWorld() != null) p.teleport(arena.getPlayerSpawn());
            plugin.getKitManager().applySelectedKit(p);
            gameKills.put(p.getUniqueId(), 0);
        }
        plugin.getMonsterManager().startForArena(arena,
                plugin.getVoteManager().isEnabled(VoteManager.Option.MONSTER_ENABLED));
        broadcast("Game started!");
        countdown = plugin.getConfig().getInt("timers.game", 300);
        runCountdown(this::endGame);
    }

    private void runCountdown(Runnable onFinish) {
        if (gameTask != null) gameTask.cancel();
        gameTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (countdown <= 0) {
                    gameTask.cancel();
                    onFinish.run();
                    return;
                }
                if (countdown == 30 || countdown == 15 || countdown <= 10) {
                    broadcast("Countdown: " + countdown + "s");
                }
                // Update bossbar progress
                double total = switch (state) {
                    case VOTING -> plugin.getConfig().getInt("timers.vote", 30);
                    case STARTING -> plugin.getConfig().getInt("timers.prestart", 15);
                    case RUNNING -> plugin.getConfig().getInt("timers.game", 300);
                    case ENDING -> plugin.getConfig().getInt("timers.end", 10);
                    default -> 1;
                };
                double progress = Math.max(0, Math.min(1, countdown / total));
                for (Player p : players) {
                    plugin.getTabUi().setBossbar(p, state + " " + countdown + "s", progress);
                }
                countdown--;
            }
        }, 0L, 20L);
    }

    private void broadcast(String message) {
        for (Player p : players) {
            p.sendMessage(message);
        }
    }

    public int getCountdown() { return countdown; }

    public void recordKill(java.util.UUID killer) {
        gameKills.merge(killer, 1, Integer::sum);
    }

    public Player getTopKiller() {
        Player bestPlayer = null;
        int best = -1;
        for (Player p : players) {
            int k = gameKills.getOrDefault(p.getUniqueId(), 0);
            if (k > best) { best = k; bestPlayer = p; }
        }
        return bestPlayer;
    }
}

