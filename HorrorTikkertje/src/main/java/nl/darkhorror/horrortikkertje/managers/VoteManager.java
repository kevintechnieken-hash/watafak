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
        inv.setItem(11, new ItemBuilder(Material.ZOMBIE_HEAD)
                .name("#FF4444Monster Enabled")
                .lore("&7Enable the AI hunter in the arena.", "&7More danger, more thrill.")
                .build());
        inv.setItem(15, new ItemBuilder(Material.NETHER_STAR)
                .name("#AA00FFCursed Arena Mode")
                .lore("&7Random arena mutations.", "&7Fog, traps, shifting walls.")
                .build());
        player.openInventory(inv);
    }

    public void toggleVote(Player player, Option option) {
        Set<UUID> set = votes.get(option);
        UUID id = player.getUniqueId();
        if (set.contains(id)) set.remove(id); else set.add(id);
        int count = set.size();
        int max = Bukkit.getOnlinePlayers().size();
        String optName = option == Option.MONSTER_ENABLED ? "Monster Enabled" : "Cursed Arena";
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ColorUtil.colorize(
                    plugin.getMessageManager().format("vote-cast",
                            java.util.Map.of("player", player.getName(), "option", optName, "count", String.valueOf(count), "max", String.valueOf(max))
                    )));
        }
    }

    public boolean isEnabled(Option option) {
        // Enabled if at least one vote, or majority - configurable later
        return !votes.get(option).isEmpty();
    }

    public void clear() { for (Option o : Option.values()) votes.get(o).clear(); }
}

