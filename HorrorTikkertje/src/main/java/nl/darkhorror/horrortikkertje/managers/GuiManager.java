package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class GuiManager {
    private final HorrorTikkertjePlugin plugin;

    public GuiManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public void openLeaderboard(Player player, String column) {
        openLeaderboard(player, column, 1);
    }

    public void openLeaderboard(Player player, String column, int page) {
        int pageSize = 45; // 5 rows for entries, last row for controls
        List<StatsManager.Record> records = plugin.getStatsManager().getLeaderboardPage(column, page, pageSize);
        Inventory inv = Bukkit.createInventory(player, 54, ColorUtil.colorize("#FFAA00Leaderboards: " + column + " #" + page));
        // Decorative border
        ItemStack glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
        for (int s = 45; s < 54; s++) inv.setItem(s, glass);
        int i = 0; int rank = (page - 1) * pageSize + 1;
        for (StatsManager.Record r : records) {
            if (i >= pageSize) break;
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            OfflinePlayer op = Bukkit.getOfflinePlayer(r.name());
            meta.setOwningPlayer(op);
            meta.setDisplayName(ColorUtil.colorize("#FFFFFF#" + rank + " " + r.name()));
            java.util.List<String> lore = new java.util.ArrayList<>();
            lore.add(ColorUtil.colorize("&7Score: &f" + r.value()));
            lore.add(ColorUtil.colorize("&7Click to view stats"));
            meta.setLore(lore);
            head.setItemMeta(meta);
            inv.setItem(i++, head);
            rank++;
        }
        // Controls: previous/next and filter buttons (wins/kills/deaths)
        inv.setItem(45, new ItemBuilder(Material.ARROW).name("#AAAAAAPrevious").build());
        inv.setItem(49, new ItemBuilder(Material.COMPASS).name("#FFFFFFFilter: " + column).lore("&7Click to cycle").build());
        inv.setItem(53, new ItemBuilder(Material.ARROW).name("#AAAAAANext").build());
        player.openInventory(inv);
    }

    public void openArenas(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ColorUtil.colorize("#55FFFFArenas"));
        int slot = 0;
        for (Arena arena : plugin.getArenaManager().getArenas()) {
            ItemStack it = new ItemBuilder(Material.MAP)
                    .name(arena.getId())
                    .lore("&7Click to set as current")
                    .build();
            inv.setItem(slot++, it);
            if (slot >= 27) break;
        }
        player.openInventory(inv);
    }
}

