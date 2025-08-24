package nl.darkhorror.horrortikkertje.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a playable kit with items and potion effects.
 */
public class Kit {
    private final String id;
    private final String displayName;
    private final Material icon;
    private final List<PotionEffect> effects;
    private final List<ItemStack> items;

    public Kit(String id, String displayName, Material icon, List<PotionEffect> effects, List<ItemStack> items) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.effects = effects != null ? effects : new ArrayList<>();
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Material getIcon() { return icon; }
    public List<PotionEffect> getEffects() { return effects; }
    public List<ItemStack> getItems() { return items; }
}

