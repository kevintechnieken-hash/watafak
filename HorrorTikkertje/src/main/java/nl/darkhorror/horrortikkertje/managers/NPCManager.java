package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Spawns and manages custom NPCs/ghosts in lobby and arenas.
 */
public class NPCManager {
    private final HorrorTikkertjePlugin plugin;
    private final java.util.List<Entity> npcs = new java.util.ArrayList<>();

    public NPCManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public Entity spawnGhost(Location location, String name) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName(ColorUtil.colorize(name));
        stand.setArms(false);
        // Simplified: no special slot disabling to maintain compatibility
        npcs.add(stand);
        return stand;
    }

    public void removeAll() {
        for (Entity e : npcs) e.remove();
        npcs.clear();
    }
}

