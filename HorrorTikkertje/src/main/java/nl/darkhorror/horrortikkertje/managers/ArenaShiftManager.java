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
            // Simple effect: toggle a small 3x3 floor at spawn between AIR and COBBLESTONE
            var base = arena.getPlayerSpawn().clone().add(-1, -1, -1);
            Material mat = Math.random() < 0.5 ? Material.AIR : Material.COBBLESTONE;
            for (int x = 0; x < 3; x++) for (int z = 0; z < 3; z++) {
                Block b = base.clone().add(x, 0, z).getBlock();
                b.setType(mat, false);
                toggled.add(b);
            }
        }, 200L, 200L);
    }

    public void stop() {
        if (task != null) { task.cancel(); task = null; }
        toggled.clear();
    }
}

