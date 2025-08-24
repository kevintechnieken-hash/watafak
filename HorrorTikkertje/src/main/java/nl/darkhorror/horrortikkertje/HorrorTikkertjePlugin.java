package nl.darkhorror.horrortikkertje;

import nl.darkhorror.horrortikkertje.api.HorrorTikkertjeAPI;
import nl.darkhorror.horrortikkertje.commands.*;
import nl.darkhorror.horrortikkertje.database.DatabaseManager;
import nl.darkhorror.horrortikkertje.listeners.GameProtectionListener;
import nl.darkhorror.horrortikkertje.listeners.MenuListener;
import nl.darkhorror.horrortikkertje.listeners.PlayerConnectionListener;
import nl.darkhorror.horrortikkertje.managers.*;
import nl.darkhorror.horrortikkertje.listeners.GameplayListener;
import nl.darkhorror.horrortikkertje.util.ColorUtil;
import nl.darkhorror.horrortikkertje.ui.TitleActionbarBossbar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for HorrorTikkertje.
 * Initializes managers, database, listeners, commands, and scheduled tasks.
 */
public final class HorrorTikkertjePlugin extends JavaPlugin {

    private static HorrorTikkertjePlugin instance;

    private ConfigManager configManager;
    private MessageManager messageManager;
    private DatabaseManager databaseManager;

    private GameManager gameManager;
    private ArenaManager arenaManager;
    private KitManager kitManager;
    private VoteManager voteManager;
    private ScoreboardManager scoreboardManager;
    private StatsManager statsManager;
    private MonsterManager monsterManager;
    private NPCManager npcManager;
    private BroadcastManager broadcastManager;
    private PowerUpManager powerUpManager;
    private HorrorEventManager horrorEventManager;
    private TitleActionbarBossbar tabUi;
    private GuiManager guiManager;
    private ArenaShiftManager arenaShiftManager;

    private HorrorTikkertjeAPI api;

    @Override
    public void onEnable() {
        instance = this;

        // Load and save default configurations
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);

        // Initialize database first
        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.initialize();

        // Initialize managers
        this.statsManager = new StatsManager(this, databaseManager);
        this.arenaManager = new ArenaManager(this);
        this.kitManager = new KitManager(this);
        this.voteManager = new VoteManager(this);
        this.gameManager = new GameManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.monsterManager = new MonsterManager(this);
        this.npcManager = new NPCManager(this);
        this.broadcastManager = new BroadcastManager(this);
        this.powerUpManager = new PowerUpManager(this);
        this.horrorEventManager = new HorrorEventManager(this);
        this.tabUi = new TitleActionbarBossbar();
        this.guiManager = new GuiManager(this);
        this.arenaShiftManager = new ArenaShiftManager(this);

        // Expose API
        this.api = new HorrorTikkertjeAPI(this);

        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameProtectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameplayListener(this), this);

        // Register commands
        registerCommands();

        // Register BungeeCord plugin messaging channel
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (channel, player, message) -> {
            // Reserved for future lobby integration
        });

        // Start scoreboard updater
        this.scoreboardManager.startUpdaterTask();
        this.powerUpManager.startSpawning();
        this.horrorEventManager.start();
        this.arenaShiftManager.start();

        getLogger().info(ColorUtil.stripHex("HorrorTikkertje enabled."));
    }

    @Override
    public void onDisable() {
        if (this.scoreboardManager != null) {
            this.scoreboardManager.stopUpdaterTask();
        }
        if (this.databaseManager != null) {
            this.databaseManager.shutdown();
        }
        if (this.horrorEventManager != null) this.horrorEventManager.stop();
        if (this.powerUpManager != null) this.powerUpManager.stopSpawning();
        if (this.arenaShiftManager != null) this.arenaShiftManager.stop();
        getLogger().info(ColorUtil.stripHex("HorrorTikkertje disabled."));
    }

    private void registerCommands() {
        getCommand("startgame").setExecutor(new StartGameCommand(this));
        getCommand("endgame").setExecutor(new EndGameCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("leaderboard").setExecutor(new LeaderboardCommand(this));
        getCommand("arenas").setExecutor(new ArenasCommand(this));
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("admin").setExecutor(new AdminCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("gameinfo").setExecutor(new GameInfoCommand(this));
        getCommand("monster").setExecutor(new MonsterCommand(this));
        getCommand("spawnnpc").setExecutor(new SpawnNpcCommand(this));
        getCommand("gameconfig").setExecutor(new GameConfigCommand(this));
    }

    public static HorrorTikkertjePlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() { return configManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public GameManager getGameManager() { return gameManager; }
    public ArenaManager getArenaManager() { return arenaManager; }
    public KitManager getKitManager() { return kitManager; }
    public VoteManager getVoteManager() { return voteManager; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public StatsManager getStatsManager() { return statsManager; }
    public MonsterManager getMonsterManager() { return monsterManager; }
    public NPCManager getNpcManager() { return npcManager; }
    public BroadcastManager getBroadcastManager() { return broadcastManager; }
    public PowerUpManager getPowerUpManager() { return powerUpManager; }
    public HorrorEventManager getHorrorEventManager() { return horrorEventManager; }
    public TitleActionbarBossbar getTabUi() { return tabUi; }
    public GuiManager getGuiManager() { return guiManager; }
    public HorrorTikkertjeAPI getApi() { return api; }
}

