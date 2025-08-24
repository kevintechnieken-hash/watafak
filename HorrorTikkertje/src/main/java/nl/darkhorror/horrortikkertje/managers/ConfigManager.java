package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Handles configuration files and provides accessors for runtime values.
 */
public class ConfigManager {

    private final HorrorTikkertjePlugin plugin;
    private File arenasFile;
    private File kitsFile;
    private File messagesFile;

    private YamlConfiguration arenasConfig;
    private YamlConfiguration kitsConfig;
    private YamlConfiguration messagesConfig;

    public ConfigManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
        setupFiles();
    }

    private void setupFiles() {
        plugin.saveResource("arenas.yml", false);
        plugin.saveResource("kits.yml", false);
        plugin.saveResource("messages.yml", false);

        this.arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
        this.kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        this.arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        this.kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadAll() {
        plugin.reloadConfig();
        this.arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        this.kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMainConfig() {
        return plugin.getConfig();
    }

    public YamlConfiguration getArenasConfig() { return arenasConfig; }
    public YamlConfiguration getKitsConfig() { return kitsConfig; }
    public YamlConfiguration getMessagesConfig() { return messagesConfig; }

    public void saveArenas() {
        try { arenasConfig.save(arenasFile); } catch (IOException ignored) {}
    }
    public void saveKits() {
        try { kitsConfig.save(kitsFile); } catch (IOException ignored) {}
    }
    public void saveMessages() {
        try { messagesConfig.save(messagesFile); } catch (IOException ignored) {}
    }
}

