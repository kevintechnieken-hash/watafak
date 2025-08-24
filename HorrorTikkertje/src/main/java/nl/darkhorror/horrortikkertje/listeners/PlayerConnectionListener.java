package nl.darkhorror.horrortikkertje.listeners;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.Map;

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
        // Broadcast join
        Map<String, String> ph = new HashMap<>();
        ph.put("player", event.getPlayer().getName());
        event.setJoinMessage(plugin.getMessageManager().format("join", ph));

        // Give lobby items with names
        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().setItem(0, new nl.darkhorror.horrortikkertje.util.ItemBuilder(org.bukkit.Material.BOOK).name("&aVoting").lore("&7Open voting menu").build());
        event.getPlayer().getInventory().setItem(1, new nl.darkhorror.horrortikkertje.util.ItemBuilder(org.bukkit.Material.CHEST).name("&aKit Selection").lore("&7Select your kit").build());
        event.getPlayer().getInventory().setItem(2, new nl.darkhorror.horrortikkertje.util.ItemBuilder(org.bukkit.Material.COMPASS).name("&aRegion Selector").lore("&7Choose your region").build());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getGameManager().removePlayer(event.getPlayer());
        Map<String, String> ph = new HashMap<>();
        ph.put("player", event.getPlayer().getName());
        event.setQuitMessage(plugin.getMessageManager().format("leave", ph));
    }
}

