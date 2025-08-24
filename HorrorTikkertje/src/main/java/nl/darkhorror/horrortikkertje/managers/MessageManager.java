package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import org.bukkit.command.CommandSender;

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
        return ColorUtil.colorize(value);
    }

    public void send(CommandSender sender, String path) {
        sender.sendMessage(getRaw(path));
    }
}

