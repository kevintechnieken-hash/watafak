package nl.darkhorror.horrortikkertje.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a playable arena with spawn points and dynamic sections.
 */
public class Arena {
    private final String id;
    private final String displayName;
    private final String worldName;
    private final Location playerSpawn;
    private final Location monsterSpawn;

    // Sections of blocks that can toggle for Arena Shifts
    private final List<Location> shiftSectionA;
    private final List<Location> shiftSectionB;

    public Arena(String id, String displayName, String worldName, Location playerSpawn, Location monsterSpawn,
                 List<Location> sectionA, List<Location> sectionB) {
        this.id = id;
        this.displayName = displayName;
        this.worldName = worldName;
        this.playerSpawn = playerSpawn;
        this.monsterSpawn = monsterSpawn;
        this.shiftSectionA = sectionA != null ? sectionA : new ArrayList<>();
        this.shiftSectionB = sectionB != null ? sectionB : new ArrayList<>();
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getWorldName() { return worldName; }
    public World getWorld() { return Bukkit.getWorld(worldName); }
    public Location getPlayerSpawn() { return playerSpawn; }
    public Location getMonsterSpawn() { return monsterSpawn; }
    public List<Location> getShiftSectionA() { return shiftSectionA; }
    public List<Location> getShiftSectionB() { return shiftSectionB; }
}

