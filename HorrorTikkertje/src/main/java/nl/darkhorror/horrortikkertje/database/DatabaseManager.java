package nl.darkhorror.horrortikkertje.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Wraps HikariCP and manages MySQL connectivity and schema setup.
 */
public class DatabaseManager {

    private final HorrorTikkertjePlugin plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initialize the connection pool and create tables if missing.
     */
    public void initialize() {
        HikariConfig config = new HikariConfig();
        String host = plugin.getConfig().getString("mysql.host", "localhost");
        int port = plugin.getConfig().getInt("mysql.port", 3306);
        String database = plugin.getConfig().getString("mysql.database", "horrortikkertje");
        String user = plugin.getConfig().getString("mysql.user", "root");
        String pass = plugin.getConfig().getString("mysql.password", "");
        boolean useSSL = plugin.getConfig().getBoolean("mysql.useSSL", false);

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL + "&serverTimezone=UTC&characterEncoding=utf8";
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(pass);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("HorrorTikkertjeHikari");

        this.dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            // Core tables
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ht_players (\n" +
                    "uuid VARCHAR(36) PRIMARY KEY,\n" +
                    "name VARCHAR(16) NOT NULL,\n" +
                    "kills INT NOT NULL DEFAULT 0,\n" +
                    "deaths INT NOT NULL DEFAULT 0,\n" +
                    "wins INT NOT NULL DEFAULT 0,\n" +
                    "playtime_seconds BIGINT NOT NULL DEFAULT 0,\n" +
                    "achievements JSON NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ht_kits (\n" +
                    "id VARCHAR(64) PRIMARY KEY,\n" +
                    "display_name VARCHAR(64) NOT NULL,\n" +
                    "icon VARCHAR(64) NOT NULL,\n" +
                    "data JSON NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ht_player_kits (\n" +
                    "uuid VARCHAR(36) NOT NULL,\n" +
                    "kit_id VARCHAR(64) NOT NULL,\n" +
                    "PRIMARY KEY (uuid, kit_id),\n" +
                    "FOREIGN KEY (kit_id) REFERENCES ht_kits(id) ON DELETE CASCADE\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ht_arenas (\n" +
                    "id VARCHAR(64) PRIMARY KEY,\n" +
                    "display_name VARCHAR(64) NOT NULL,\n" +
                    "world VARCHAR(64) NOT NULL,\n" +
                    "data JSON NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ht_votes (\n" +
                    "arena_id VARCHAR(64) NOT NULL,\n" +
                    "option_key VARCHAR(64) NOT NULL,\n" +
                    "votes INT NOT NULL DEFAULT 0,\n" +
                    "PRIMARY KEY (arena_id, option_key)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to setup database: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

