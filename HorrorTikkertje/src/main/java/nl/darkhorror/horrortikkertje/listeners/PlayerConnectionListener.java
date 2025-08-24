package nl.darkhorror.horrortikkertje.listeners;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles player join/leave, initializes stats and lobby visuals.
 */
public class PlayerConnectionListener implements Listener {
    private final HorrorTikkertjePlugin plugin;

    public PlayerConnectionListener(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getStatsManager().ensurePlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        plugin.getGameManager().addPlayer(event.getPlayer());
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getGameManager().removePlayer(event.getPlayer());
        event.setQuitMessage(null);
    }
}

