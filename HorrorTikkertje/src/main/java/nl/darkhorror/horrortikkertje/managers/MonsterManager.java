package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Controls AI monster lifecycle and behavior hooks.
 */
public class MonsterManager {
    private final HorrorTikkertjePlugin plugin;
    private Mob activeMonster;
    private BukkitTask chaseTask;

    public MonsterManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnPreGame(Arena arena) {
        if (arena == null || arena.getWorld() == null) return;
        if (activeMonster != null && !activeMonster.isDead()) activeMonster.remove();
        World world = arena.getWorld();
        Location spawn = arena.getMonsterSpawn();
        activeMonster = (Mob) world.spawnEntity(spawn, EntityType.ZOMBIE);
        configureMonster(activeMonster, true);
    }

    public void startForArena(Arena arena, boolean enabled) {
        if (!enabled) {
            removeMonster();
            return;
        }
        if (activeMonster == null || activeMonster.isDead()) {
            spawnPreGame(arena);
        }
        if (chaseTask != null) chaseTask.cancel();
        chaseTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (activeMonster == null || activeMonster.isDead()) return;
            Player nearest = null;
            double best = Double.MAX_VALUE;
            for (Player p : plugin.getGameManager().getPlayers()) {
                if (!p.getWorld().equals(activeMonster.getWorld())) continue;
                double d = p.getLocation().distanceSquared(activeMonster.getLocation());
                if (d < best) { best = d; nearest = p; }
            }
            if (nearest != null) {
                activeMonster.setTarget(nearest);
            }
        }, 20L, 20L);
    }

    private void configureMonster(Mob mob, boolean preGame) {
        if (mob.getAttribute(Attribute.MAX_HEALTH) != null) {
            mob.getAttribute(Attribute.MAX_HEALTH).setBaseValue(60.0);
        }
        mob.setHealth(Math.min(60.0, mob.getMaxHealth()));
        if (mob.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
            mob.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.35);
        }
        mob.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        if (preGame) {
            mob.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 15, 0, true, false));
        }
    }

    public void removeMonster() {
        if (activeMonster != null) {
            activeMonster.remove();
            activeMonster = null;
        }
        if (chaseTask != null) {
            chaseTask.cancel();
            chaseTask = null;
        }
    }
}

