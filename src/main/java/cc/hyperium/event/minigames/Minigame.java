package cc.hyperium.event.minigames;

/**
 * Represents a Hypixel minigame
 */
public enum Minigame {

    QUAKECRAFT("Quakecraft", "Quake", 2),

    WALLS("Walls", "Walls", 3),

    PAINTBALL("Paintball", "Paintball", 2),

    SURVIVAL_GAMES("Blitz SG", "HungerGames", 8),

    TNTGAMES("The TNT Games", "TNTGames", 13),

    VAMPIREZ("VampireZ", "VampireZ", 6),

    WALLS3("Mega Walls", "Walls3", 6),

    ARCADE("Arcade Games", "Arcade", 0),

    ARENA("Arena Brawl", "Arena", 2),

    MCGO("Cops and Crims", "MCGO", 4),

    UHC("UHC Champions", "UHC", 14),

    BATTLEGROUND("Warlords", "Battleground", 15),

    SUPER_SMASH("Smash Heroes", "SuperSmash", 11),

    GINGERBREAD("Turbo Kart Racers", "GingerBread", 2),

    HOUSING("Housing", "Housing", 5),

    SKYWARS("SkyWars", "SkyWars", 10),

    TRUE_COMBAT("Crazy Walls", "TrueCombat", 2),

    SPEED_UHC("Speed UHC", "SpeedUHC", 12),

    SKYCLASH("SkyClash", "SkyClash", 9),

    LEGACY("Classic Games", "Legacy", 2),

    PROTOTYPE("Prototype", "Prototype", 7),

    BEDWARS("Bed Wars", "Bedwars", 1),

    MURDER_MYSTERY("Murder Mystery", "MurderMystery", 17),

    BUILD_BATTLE("Build Battle", "BuildBattle", 7),

    DUELS("Duels", "Duels", 7);

    /**
     * The scoreboard name
     */
    public final String scoreName;

    /**
     * The name used to store data
     */
    public final String dbName;

    /**
     * The game ID
     */
    public final int id;

    Minigame(String scoreName, String dbName, int id) {
        this.scoreName = scoreName;
        this.dbName = dbName;
        this.id = id;
    }

    public String getScoreName() {
        return scoreName;
    }

    public String getDbName() {
        return dbName;
    }

    public int getId() {
        return id;
    }
}
