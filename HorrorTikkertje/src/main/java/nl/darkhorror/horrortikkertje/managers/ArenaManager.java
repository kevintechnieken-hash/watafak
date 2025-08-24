package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.model.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

/**
 * Manages arenas and selections.
 */
public class ArenaManager {
    private final HorrorTikkertjePlugin plugin;
    private final Map<String, Arena> idToArena = new LinkedHashMap<>();
    private Arena currentArena;

    public ArenaManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        idToArena.clear();
        YamlConfiguration cfg = plugin.getConfigManager().getArenasConfig();
        ConfigurationSection root = cfg.getConfigurationSection("arenas");
        if (root == null) return;
        for (String id : root.getKeys(false)) {
            ConfigurationSection sec = root.getConfigurationSection(id);
            String display = sec.getString("display", id);
            String worldName = sec.getString("world", "world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            Location spawn = new Location(world,
                    sec.getConfigurationSection("spawn").getDouble("x", 0),
                    sec.getConfigurationSection("spawn").getDouble("y", 64),
                    sec.getConfigurationSection("spawn").getDouble("z", 0));
            Location mspawn = new Location(world,
                    sec.getConfigurationSection("monster-spawn").getDouble("x", 0),
                    sec.getConfigurationSection("monster-spawn").getDouble("y", 64),
                    sec.getConfigurationSection("monster-spawn").getDouble("z", 0));

            List<Location> sectionA = new ArrayList<>();
            List<Location> sectionB = new ArrayList<>();
            idToArena.put(id, new Arena(id, display, worldName, spawn, mspawn, sectionA, sectionB));
        }
        // Select default current arena if any
        if (currentArena == null && !idToArena.isEmpty()) {
            currentArena = idToArena.values().iterator().next();
        }
    }

    public Collection<Arena> getArenas() { return Collections.unmodifiableCollection(idToArena.values()); }
    public Arena getArena(String id) { return idToArena.get(id); }
    public Arena getCurrentArena() { return currentArena; }
    public void setCurrentArena(Arena arena) { this.currentArena = arena; }
}

