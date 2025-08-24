package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;

/**
 * Provides access to colored and templated messages from messages.yml
 */
public class MessageManager {

    private final HorrorTikkertjePlugin plugin;

    public MessageManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public String getRaw(String path) {
        String value = plugin.getConfigManager().getMessagesConfig().getString(path, "&7Unknown message: " + path);
        return ColorUtil.colorize(applyPrefix(value));
    }

    public void send(CommandSender sender, String path) {
        sender.sendMessage(getRaw(path));
    }

    public String format(String path, Map<String, String> placeholders) {
        String template = plugin.getConfigManager().getMessagesConfig().getString(path, "&7Unknown message: " + path);
        template = applyPrefix(template);
        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                template = template.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
            }
        }
        return ColorUtil.colorize(template);
    }

    private String applyPrefix(String input) {
        YamlConfiguration y = plugin.getConfigManager().getMessagesConfig();
        String prefix = y.getString("prefix", "");
        return input.replace("{prefix}", prefix);
    }
}

