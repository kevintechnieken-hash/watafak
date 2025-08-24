package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Kit;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Handles kits data and assignments.
 */
public class KitManager {
    private final HorrorTikkertjePlugin plugin;
    private final Map<String, Kit> idToKit = new LinkedHashMap<>();
    private final Map<UUID, Kit> playerSelectedKit = new HashMap<>();

    public KitManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        idToKit.clear();
        YamlConfiguration cfg = plugin.getConfigManager().getKitsConfig();
        ConfigurationSection root = cfg.getConfigurationSection("kits");
        if (root == null) return;
        for (String id : root.getKeys(false)) {
            ConfigurationSection sec = root.getConfigurationSection(id);
            String display = sec.getString("display", id);
            Material icon = Material.matchMaterial(sec.getString("icon", "STONE"));
            List<PotionEffect> effects = new ArrayList<>();
            for (String e : sec.getStringList("effects")) {
                String[] parts = e.split(":");
                PotionEffectType type = PotionEffectType.getByName(parts[0].toUpperCase());
                int amplifier = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                if (type != null) effects.add(new PotionEffect(type, Integer.MAX_VALUE, amplifier, true, false));
            }
            List<ItemStack> items = new ArrayList<>();
            for (String it : sec.getStringList("items")) {
                String[] parts = it.split(":");
                Material mat = Material.matchMaterial(parts[0].toUpperCase());
                int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
                if (mat != null) items.add(new ItemStack(mat, amount));
            }
            idToKit.put(id, new Kit(id, display, icon != null ? icon : Material.STONE, effects, items));
        }
    }

    public void openKitMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ColorUtil.colorize("#FF5555Select Kit"));
        int slot = 10;
        for (Kit kit : idToKit.values()) {
            java.util.List<String> lore = new java.util.ArrayList<>();
            lore.add("&7Click to select");
            if (!kit.getEffects().isEmpty()) lore.add("&7Effects:");
            for (var e : kit.getEffects()) {
                lore.add(" &f" + e.getType().getName().toLowerCase().replace('_',' ') + " " + (e.getAmplifier() + 1));
            }
            if (!kit.getItems().isEmpty()) lore.add("&7Items:");
            for (var it : kit.getItems()) lore.add(" &fx" + it.getAmount() + " " + it.getType().name().toLowerCase());

            ItemBuilder builder = new ItemBuilder(kit.getIcon())
                    .name(kit.getDisplayName())
                    .lore(lore.toArray(new String[0]))
                    .glow();
            inv.setItem(slot++, builder.build());
            if (slot % 9 == 8) slot += 2;
        }
        player.openInventory(inv);
    }

    public void applySelectedKit(Player player) {
        Kit kit = playerSelectedKit.get(player.getUniqueId());
        if (kit == null) return;
        for (PotionEffect effect : kit.getEffects()) {
            player.addPotionEffect(effect, true);
        }
        for (ItemStack item : kit.getItems()) {
            player.getInventory().addItem(item);
        }
    }

    public boolean isKitItem(ItemStack stack) {
        if (stack == null) return false;
        for (Kit kit : idToKit.values()) {
            if (stack.getType() == kit.getIcon()) return true;
        }
        return false;
    }

    public void handleKitClick(Player player, ItemStack currentItem) {
        if (currentItem == null) return;
        for (Kit kit : idToKit.values()) {
            if (currentItem.getType() == kit.getIcon()) {
                playerSelectedKit.put(player.getUniqueId(), kit);
                player.sendMessage(ColorUtil.colorize("#55FF55Geselecteerde kit: &f" + kit.getDisplayName()));
                player.closeInventory();
                return;
            }
        }
    }

    public Kit getSelectedKit(UUID uuid) { return playerSelectedKit.get(uuid); }
    public Collection<Kit> getKits() { return Collections.unmodifiableCollection(idToKit.values()); }
}

