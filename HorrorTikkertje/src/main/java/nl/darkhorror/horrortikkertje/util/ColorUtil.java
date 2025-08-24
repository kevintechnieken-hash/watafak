package nl.darkhorror.horrortikkertje.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for translating RGB/hex colors and standard color codes.
 */
public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    private ColorUtil() {}

    /**
     * Translates hex codes (#RRGGBB) and legacy color codes (&) to Bukkit-compatible string.
     */
    public static String colorize(String input) {
        if (input == null) return "";
        String hexTranslated = translateHex(input);
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', hexTranslated);
    }

    /**
     * Removes hex patterns to a plain string (for console without colors).
     */
    public static String stripHex(String input) {
        if (input == null) return "";
        return HEX_PATTERN.matcher(input).replaceAll("");
    }

    private static String translateHex(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            ChatColor color = ChatColor.of('#' + matcher.group(1));
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}

