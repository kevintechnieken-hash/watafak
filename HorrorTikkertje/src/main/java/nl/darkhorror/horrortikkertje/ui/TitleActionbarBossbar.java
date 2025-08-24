package nl.darkhorror.horrortikkertje.ui;

import nl.darkhorror.horrortikkertje.util.ColorUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitleActionbarBossbar {
    private final Map<UUID, BossBar> playerBars = new HashMap<>();

    public void showTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(ColorUtil.colorize(title), ColorUtil.colorize(subtitle), fadeIn, stay, fadeOut);
    }

    public void sendActionbar(Player player, String message) {
        // Fallback to legacy API for maximum compatibility
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(ColorUtil.colorize(message)));
    }

    public void setBossbar(Player player, String text, double progress) {
        BossBar bar = playerBars.computeIfAbsent(player.getUniqueId(), id -> Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID));
        bar.setProgress(Math.max(0, Math.min(1, progress)));
        bar.setTitle(ColorUtil.colorize(text));
        if (!bar.getPlayers().contains(player)) bar.addPlayer(player);
        bar.setVisible(true);
    }

    public void setBossbar(Player player, String text, double progress, BarColor color) {
        BossBar bar = playerBars.computeIfAbsent(player.getUniqueId(), id -> Bukkit.createBossBar("", color, BarStyle.SOLID));
        bar.setColor(color);
        bar.setProgress(Math.max(0, Math.min(1, progress)));
        bar.setTitle(ColorUtil.colorize(text));
        if (!bar.getPlayers().contains(player)) bar.addPlayer(player);
        bar.setVisible(true);
    }

    public void clearBossbar(Player player) {
        BossBar bar = playerBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
            bar.setVisible(false);
        }
    }
}

