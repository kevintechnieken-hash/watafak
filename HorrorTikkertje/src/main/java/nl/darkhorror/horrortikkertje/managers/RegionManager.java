package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RegionManager {
    public enum Region { ASIA, EUROPE, NORTH_AMERICA, MIDDLE_EAST }

    private final HorrorTikkertjePlugin plugin;
    private Region currentRegion = Region.EUROPE;

    public RegionManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public Region getCurrentRegion() { return currentRegion; }

    public void setCurrentRegion(Region region) {
        this.currentRegion = region;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ColorUtil.colorize(plugin.getMessageManager().format("region-set",
                    java.util.Map.of("region", toDisplay(region)))));
        }
    }

    public void openRegionMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ColorUtil.colorize("#55FF55Region Selector"));
        inv.setItem(10, new ItemBuilder(Material.ENDER_PEARL).name("#00FFFFAsia").lore("&7Lower latency in Asia").build());
        inv.setItem(12, new ItemBuilder(Material.LAPIS_LAZULI).name("#00AAFFEurope").lore("&7Lower latency in Europe").build());
        inv.setItem(14, new ItemBuilder(Material.REDSTONE).name("#FF5555North-America").lore("&7Lower latency in NA").build());
        inv.setItem(16, new ItemBuilder(Material.SAND).name("#FFAA00Middle East").lore("&7Lower latency in ME").build());
        player.openInventory(inv);
    }

    public void handleRegionClick(Player player, org.bukkit.inventory.ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        String name = ColorUtil.stripHex(item.getItemMeta().getDisplayName()).toLowerCase();
        if (name.contains("asia")) setCurrentRegion(Region.ASIA);
        else if (name.contains("europe")) setCurrentRegion(Region.EUROPE);
        else if (name.contains("north-america")) setCurrentRegion(Region.NORTH_AMERICA);
        else if (name.contains("middle east")) setCurrentRegion(Region.MIDDLE_EAST);
        player.closeInventory();
    }

    public String toDisplay(Region region) {
        return switch (region) {
            case ASIA -> "Asia";
            case EUROPE -> "Europe";
            case NORTH_AMERICA -> "North-America";
            case MIDDLE_EAST -> "Middle East";
        };
    }
}

