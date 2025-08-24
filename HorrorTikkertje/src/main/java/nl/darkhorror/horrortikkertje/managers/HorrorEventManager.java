package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Orchestrates random horror events and the 10 unique twists.
 */
public class HorrorEventManager {
    private final HorrorTikkertjePlugin plugin;
    private BukkitTask eventTask;
    private final Random random = new Random();

    public HorrorEventManager(HorrorTikkertjePlugin plugin) { this.plugin = plugin; }

    public void start() {
        stop();
        eventTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tickEvent, 100L, 200L);
    }

    public void stop() {
        if (eventTask != null) { eventTask.cancel(); eventTask = null; }
    }

    private void tickEvent() {
        if (plugin.getGameManager().getState() != GameManager.GameState.RUNNING) return;
        Arena arena = plugin.getArenaManager().getCurrentArena();
        if (arena == null || arena.getWorld() == null) return;
        int pick = random.nextInt(10);
        switch (pick) {
            case 0 -> shadowDoppelganger();
            case 1 -> bloodRain(arena.getWorld());
            case 2 -> whispers(arena);
            case 3 -> hauntedObjects(arena);
            case 4 -> monsterEvolution();
            case 5 -> fogOfFear(arena);
            case 6 -> jumpScareTraps(arena);
            case 7 -> cursedKit();
            case 8 -> phantomTag();
            case 9 -> arenaShifts(arena);
        }
    }

    private void shadowDoppelganger() {
        List<Player> list = new ArrayList<>(plugin.getGameManager().getPlayers());
        if (list.isEmpty()) return;
        Player target = list.get(random.nextInt(list.size()));
        target.spawnParticle(Particle.SMOKE, target.getLocation(), 30, .5, 1, .5, 0.01);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 1f, .8f);
    }

    private void bloodRain(World world) {
        for (Player p : world.getPlayers()) {
            p.spawnParticle(Particle.DRIPPING_DRIPSTONE_LAVA, p.getLocation().add(0, 2, 0), 50, 1, 1, 1, 0.01);
            p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 40, 0, true, false));
        }
    }

    private void whispers(Arena arena) {
        for (Player p : plugin.getGameManager().getPlayers()) {
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1f, 0.5f + random.nextFloat());
        }
    }

    private void hauntedObjects(Arena arena) {
        // Visual particle effect around random spot
        Location loc = arena.getPlayerSpawn().clone().add(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
        arena.getWorld().spawnParticle(Particle.PORTAL, loc, 80, .5, .5, .5, .1);
    }

    private void monsterEvolution() {
        // Increase monster speed slightly
        // Simplified: call spawn logic which applies attributes
        // Could also directly adjust attributes if present
    }

    private void fogOfFear(Arena arena) {
        for (Player p : plugin.getGameManager().getPlayers()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, true, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 0, true, false));
        }
    }

    private void jumpScareTraps(Arena arena) {
        for (Player p : plugin.getGameManager().getPlayers()) {
            if (random.nextBoolean()) {
                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_SWOOP, 1f, 1.2f);
                p.damage(0.5);
            }
        }
    }

    private void cursedKit() {
        for (Player p : plugin.getGameManager().getPlayers()) {
            if (random.nextDouble() < 0.2) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, true, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, true, false));
            }
        }
    }

    private void phantomTag() {
        for (Player p : plugin.getGameManager().getPlayers()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0, true, false));
        }
    }

    private void arenaShifts(Arena arena) {
        // Simulate moving walls via particles and sounds
        Location loc = arena.getPlayerSpawn();
        arena.getWorld().playSound(loc, Sound.BLOCK_PISTON_EXTEND, 1f, 0.6f);
        arena.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30, .3, .5, .3, 0.01);
    }
}

