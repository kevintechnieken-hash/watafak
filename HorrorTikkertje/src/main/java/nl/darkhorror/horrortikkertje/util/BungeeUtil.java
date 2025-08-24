package nl.darkhorror.horrortikkertje.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Utility to send players across BungeeCord network.
 */
public final class BungeeUtil {
    private BungeeUtil() {}

    public static void connect(Player player, String server, Plugin plugin) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        } catch (Exception ignored) {}
    }
}

