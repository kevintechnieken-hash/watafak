package nl.darkhorror.horrortikkertje.managers;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Persists and loads player statistics.
 */
public class StatsManager {
    private final HorrorTikkertjePlugin plugin;
    private final DatabaseManager databaseManager;

    public StatsManager(HorrorTikkertjePlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void ensurePlayer(UUID uuid, String name) {
        try (Connection c = databaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO ht_players (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("ensurePlayer failed: " + e.getMessage());
        }
    }

    public int getStat(UUID uuid, String column) {
        try (Connection c = databaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + column + " FROM ht_players WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("getStat failed: " + e.getMessage());
        }
        return 0;
    }

    public void incrementStat(UUID uuid, String column, int amount) {
        try (Connection c = databaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE ht_players SET " + column + " = " + column + " + ? WHERE uuid=?")) {
            ps.setInt(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("incrementStat failed: " + e.getMessage());
        }
    }

    public java.util.List<String> getLeaderboard(String column, int limit) {
        java.util.List<String> result = new java.util.ArrayList<>();
        String sql = "SELECT name, " + column + " FROM ht_players ORDER BY " + column + " DESC LIMIT ?";
        try (Connection c = databaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    result.add("#" + i++ + " " + rs.getString(1) + ": " + rs.getInt(2));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("getLeaderboard failed: " + e.getMessage());
        }
        return result;
    }
}

