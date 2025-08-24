package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Toggles sets of blocks to simulate moving walls/platforms.
 */
public class ArenaShiftManager {
    private final HorrorTikkertjePlugin plugin;
    private BukkitTask task;
    private final List<Block> toggled = new ArrayList<>();

    public ArenaShiftManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public void start() {
        stop();
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.getGameManager().getState() != GameManager.GameState.RUNNING) return;
            Arena arena = plugin.getArenaManager().getCurrentArena();
            if (arena == null || arena.getWorld() == null) return;
            // Use configured sections A and B, toggle them to random materials
            Material matA = Math.random() < 0.5 ? Material.AIR : Material.COBBLESTONE;
            Material matB = Math.random() < 0.5 ? Material.AIR : Material.OAK_PLANKS;
            arena.getShiftSectionA().forEach(loc -> loc.getBlock().setType(matA, false));
            arena.getShiftSectionB().forEach(loc -> loc.getBlock().setType(matB, false));
        }, 200L, 200L);
    }

    public void stop() {
        if (task != null) { task.cancel(); task = null; }
        toggled.clear();
    }
}

