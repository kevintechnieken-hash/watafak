package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * Tracks votes for arena options and game modifiers.
 */
public class VoteManager {
    public enum Option { MONSTER_ENABLED, CURSED_ARENA }

    private final HorrorTikkertjePlugin plugin;
    private final Map<Option, Set<UUID>> votes = new EnumMap<>(Option.class);

    public VoteManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
        for (Option option : Option.values()) votes.put(option, new HashSet<>());
    }

    public void openVoteMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, ColorUtil.colorize("#00AAAAVoting"));
        inv.setItem(11, new ItemBuilder(Material.ZOMBIE_HEAD).name("#FF4444Monster Enabled").lore("&7Stem om het monster te activeren").build());
        inv.setItem(15, new ItemBuilder(Material.NETHER_STAR).name("#AA00FFCursed Arena Mode").lore("&7Random arena effecten").build());
        player.openInventory(inv);
    }

    public void toggleVote(Player player, Option option) {
        Set<UUID> set = votes.get(option);
        UUID id = player.getUniqueId();
        if (set.contains(id)) set.remove(id); else set.add(id);
        int count = set.size();
        player.sendMessage(ColorUtil.colorize("#FFFF55Je stem voor &f" + option + " &7is nu " + (set.contains(id) ? "&aAAN" : "&cUIT") + " &7(" + count + " stemmen)"));
    }

    public boolean isEnabled(Option option) {
        // Enabled if at least one vote, or majority - configurable later
        return !votes.get(option).isEmpty();
    }

    public void clear() { for (Option o : Option.values()) votes.get(o).clear(); }
}

