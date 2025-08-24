package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

/**
 * Spawns and applies horror-themed power-ups.
 */
public class PowerUpManager {
    private final HorrorTikkertjePlugin plugin;
    private final Set<Item> activeDrops = new HashSet<>();
    private BukkitTask spawnTask;

    public PowerUpManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public void startSpawning() {
        stopSpawning();
        spawnTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.getGameManager().getState() != GameManager.GameState.RUNNING) return;
            var arena = plugin.getArenaManager().getCurrentArena();
            if (arena == null || arena.getWorld() == null) return;
            Location loc = arena.getPlayerSpawn().clone().add((Math.random() - 0.5) * 10, 0, (Math.random() - 0.5) * 10);
            ItemStack stack = new ItemBuilder(Material.NETHER_WART).name("#AA0000Cursed Power").lore("&7Pak op voor een boost").glow().build();
            Item dropped = arena.getWorld().dropItemNaturally(loc, stack);
            activeDrops.add(dropped);
        }, 200L, 200L);
    }

    public void stopSpawning() {
        if (spawnTask != null) { spawnTask.cancel(); spawnTask = null; }
        for (Item item : activeDrops) item.remove();
        activeDrops.clear();
    }

    public boolean tryConsume(Player player, Item item) {
        if (!activeDrops.remove(item)) return false;
        item.remove();
        // Random power-up
        double r = Math.random();
        if (r < 0.25) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 0, true, false)); // Ghost mode
        } else if (r < 0.5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 2, true, false));
        } else if (r < 0.75) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 15, 1, true, false));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 0, true, false));
        }
        return true;
    }
}

