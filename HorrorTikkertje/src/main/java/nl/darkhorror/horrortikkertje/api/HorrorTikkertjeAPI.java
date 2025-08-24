package nl.darkhorror.horrortikkertje.api;

import nl.darkhorror.horrortikkertje.HorrorTikkertjePlugin;
import nl.darkhorror.horrortikkertje.managers.*;

/**
 * Public API facade for integrations.
 */
public class HorrorTikkertjeAPI {
    private final HorrorTikkertjePlugin plugin;

    public HorrorTikkertjeAPI(HorrorTikkertjePlugin plugin) {
        this.plugin = plugin;
    }

    public GameManager getGameManager() { return plugin.getGameManager(); }
    public ArenaManager getArenaManager() { return plugin.getArenaManager(); }
    public KitManager getKitManager() { return plugin.getKitManager(); }
    public VoteManager getVoteManager() { return plugin.getVoteManager(); }
    public ScoreboardManager getScoreboardManager() { return plugin.getScoreboardManager(); }
    public StatsManager getStatsManager() { return plugin.getStatsManager(); }
    public MonsterManager getMonsterManager() { return plugin.getMonsterManager(); }
    public NPCManager getNpcManager() { return plugin.getNpcManager(); }
}

