package nl.darkhorror.horrortikkertje.listeners;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.managers.KitManager;
import nl.darkhorror.horrortikkertje.managers.VoteManager;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Handles menu interactions for kit selection and voting.
 */
public class MenuListener implements Listener {
    private final HorrorTikkertjePlugin plugin;

    public MenuListener(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory inv = event.getInventory();
        String title = ColorUtil.stripHex(event.getView().getTitle());
        ItemStack current = event.getCurrentItem();

        if (title.contains("Select Kit")) {
            event.setCancelled(true);
            plugin.getKitManager().handleKitClick(player, current);
            return;
        }

        if (title.contains("Voting")) {
            event.setCancelled(true);
            if (current == null) return;
            if (current.getType() == Material.ZOMBIE_HEAD) {
                plugin.getVoteManager().toggleVote(player, VoteManager.Option.MONSTER_ENABLED);
            } else if (current.getType() == Material.NETHER_STAR) {
                plugin.getVoteManager().toggleVote(player, VoteManager.Option.CURSED_ARENA);
            }
        }
    }
}

