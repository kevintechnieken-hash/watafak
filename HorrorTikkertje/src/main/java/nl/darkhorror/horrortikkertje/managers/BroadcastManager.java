package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import org.bukkit.Bukkit;

/**
 * Centralized broadcasts and announcement utilities.
 */
public class BroadcastManager {
    private final HorrorTikkertjePlugin plugin;

    public BroadcastManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(ColorUtil.colorize(message));
    }
}

