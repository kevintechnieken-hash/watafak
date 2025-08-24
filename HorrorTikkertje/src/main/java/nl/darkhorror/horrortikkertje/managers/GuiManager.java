package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiManager {
    private final HorrorTikkertjePlugin plugin;

    public GuiManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public void openLeaderboard(Player player, String column) {
        List<StatsManager.Record> records = plugin.getStatsManager().getLeaderboardRecords(column, 27);
        Inventory inv = Bukkit.createInventory(player, 27, ColorUtil.colorize("#FFAA00Leaderboards: " + column));
        int i = 0; int rank = 1;
        for (StatsManager.Record r : records) {
            if (i >= 27) break;
            ItemStack it = new ItemBuilder(Material.PAPER)
                    .name("#FFFFFF#" + rank + " " + r.name())
                    .lore("&7Score: &f" + r.value())
                    .build();
            inv.setItem(i++, it);
            rank++;
        }
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

