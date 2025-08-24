package nl.darkhorror.horrortikkertje.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ColorUtil.colorize(name));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lines) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.stream(lines).map(ColorUtil::colorize).toList());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder glow() {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() { return stack; }
}

