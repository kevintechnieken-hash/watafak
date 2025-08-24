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

        if (title.contains("Leaderboards")) {
            event.setCancelled(true);
            if (current == null) return;
            // Parse column and page from title
            String[] parts = title.split("#");
            String column = "wins";
            int page = 1;
            if (parts.length >= 2) {
                String left = parts[0];
                if (left.contains(":")) column = ColorUtil.stripHex(left.substring(left.indexOf(":") + 1)).trim();
                try { page = Integer.parseInt(parts[1].trim()); } catch (Exception ignored) {}
            }
            int slot = event.getRawSlot();
            if (slot == 45) {
                page = Math.max(1, page - 1);
                plugin.getGuiManager().openLeaderboard(player, column, page);
            } else if (slot == 53) {
                page = page + 1;
                plugin.getGuiManager().openLeaderboard(player, column, page);
            } else if (slot == 49) {
                // cycle filters
                column = switch (column.toLowerCase()) {
                    case "wins" -> "kills";
                    case "kills" -> "deaths";
                    default -> "wins";
                };
                plugin.getGuiManager().openLeaderboard(player, column, 1);
            }
        }

        if (title.contains("Arenas")) {
            event.setCancelled(true);
            if (current == null || current.getType() != Material.MAP) return;
            String id = current.hasItemMeta() && current.getItemMeta().hasDisplayName() ? current.getItemMeta().getDisplayName() : null;
            if (id == null) return;
            var arena = plugin.getArenaManager().getArena(ColorUtil.stripHex(id));
            if (arena == null) { player.sendMessage(ColorUtil.colorize("#FF5555Arena not found")); return; }
            if (!player.hasPermission("horrortikkertje.admin")) { player.sendMessage("No permission."); return; }
            plugin.getArenaManager().setCurrentArena(arena);
            player.sendMessage(ColorUtil.colorize("#55FF55Arena set: &f" + arena.getId()));
            player.closeInventory();
        }

        if (title.contains("Region Selector")) {
            event.setCancelled(true);
            plugin.getRegionManager().handleRegionClick(player, current);
        }
    }
}

