package nl.darkhorror.horrortikkertje.listeners;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.Bukkit;
import nl.darkhorror.horrortikkertje.managers.GameManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Handles tag logic and power-up pickups.
 */
public class GameplayListener implements Listener {
    private final HorrorTikkertjePlugin plugin;

    public GameplayListener(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onTag(EntityDamageByEntityEvent event) {
        if (plugin.getGameManager().getState() != GameManager.GameState.RUNNING) return;
        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player damager) {
            // Tag event: count kill as a tag
            plugin.getStatsManager().incrementStat(damager.getUniqueId(), "kills", 1);
            plugin.getStatsManager().incrementStat(victim.getUniqueId(), "deaths", 1);
            plugin.getGameManager().recordKill(damager.getUniqueId());
            victim.damage(0); // no extra damage; already counted
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        if (plugin.getPowerUpManager().tryConsume(player, item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getGameManager().getState() != GameManager.GameState.RUNNING) return;
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
            plugin.getTabUi().sendActionbar(player, "&7You are eliminated. Use &a/leave &7or &a/hub &7to return.");
        });
    }
}

