package nl.darkhorror.horrortikkertje.listeners;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.managers.GameManager;
import nl.darkhorror.horrortikkertje.managers.KitManager;
import nl.darkhorror.horrortikkertje.managers.VoteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Protects lobby/voting from item drops and block changes.
 */
public class GameProtectionListener implements Listener {
    private final HorrorTikkertjePlugin plugin;

    public GameProtectionListener(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        GameManager.GameState state = plugin.getGameManager().getState();
        if (state == GameManager.GameState.LOBBY || state == GameManager.GameState.VOTING || state == GameManager.GameState.STARTING || state == GameManager.GameState.ENDING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        GameManager.GameState state = plugin.getGameManager().getState();
        if (state != GameManager.GameState.RUNNING) event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        GameManager.GameState state = plugin.getGameManager().getState();
        if (state != GameManager.GameState.RUNNING) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        GameManager.GameState state = plugin.getGameManager().getState();
        if (state == GameManager.GameState.LOBBY || state == GameManager.GameState.VOTING || state == GameManager.GameState.STARTING) {
            if (event.getItem() != null) {
                switch (event.getItem().getType()) {
                    case BOOK -> plugin.getVoteManager().openVoteMenu(event.getPlayer());
                    case CHEST -> plugin.getKitManager().openKitMenu(event.getPlayer());
                }
            }
        }
    }
}

