package net.hypixel.api;


import java.util.Locale;

public enum GameType {
    UNKNOWN("Unknown", "ERROR", -1),
    QUAKECRAFT("Quakecraft", "Quake", 2),
    WALLS("Walls", "Walls", 3),
    PAINTBALL("Paintball", "Paintball", 4),
    SURVIVAL_GAMES("Blitz Survival Games", "HungerGames", 5),
    TNTGAMES("The TNT Games", "TNTGames", 6),
    VAMPIREZ("VampireZ", "VampireZ", 7),
    WALLS3("Mega Walls", "Walls3", 13),
    ARCADE("Arcade", "Arcade", 14),
    ARENA("Arena Brawl", "Arena", 17),
    MCGO("Cops and Crims", "MCGO", 21),
    UHC("UHC Champions", "UHC", 20),
    BATTLEGROUND("Warlords", "Battleground", 23),
    SUPER_SMASH("Smash Heroes", "SuperSmash", 24),
    GINGERBREAD("Turbo Kart Racers", "GingerBread", 25),
    SKYWARS("SkyWars", "SkyWars", 51),
    TRUE_COMBAT("Crazy Walls", "TrueCombat", 52),
    SKYCLASH("SkyClash", "SkyClash", 55),
    LEGACY("Classic Games", "Legacy", 56),
    SPEED_UHC("Speed UHC", "SpeedUHC", 54),
    BEDWARS("Bedwars", "Bedwars", 57),
    MURDER_MYSTERY("Murder Mystery", "MurderMystery", 59),
    BUILD_BATTLE("Build Battle", "BuildBattle", 60),
    DUELS("Duels", "Duels", 61),
    THE_PIT("The Pit", "PIT", 101);

    private static final GameType[] VALUES = values();

    private final String name, dbName;
    private final Integer id;

    GameType(String name, String dbName, Integer id) {
        this.name = name;
        this.dbName = dbName;
        this.id = id;

    }

    /**
     * @param id The internal id
     * @return The GameType associated with that id, or null if there isn't one.
     */
    public static GameType fromId(int id) {
        for (GameType gameType : VALUES) {
            if (gameType.id == id) {
                return gameType;
            }
        }

        return null;
    }

    /**
     * @param dbName The key used in the database
     * @return The GameType associated with that key, or null if there isn't one.
     */
    public static GameType fromDatabase(String dbName) {
        for (GameType gameType : VALUES) {
            if (gameType.dbName.equals(dbName)) {
                return gameType;
            }
        }

        // Hyperium
        return UNKNOWN;
    }

    /**
     * Exposing this method allows people to use the array without cloning.
     * Slightly faster but not as safe since the array could be modified.
     */
    public static GameType[] getValues() {
        return VALUES;
    }

    /**
     * @return The official name of the GameType
     */
    public String getName() {
        return name;
    }

    /**
     * @return The internal ID that is occasionally used in various database schemas
     */
    public int getId() {
        return id;
    }

    public String getDbName() {
        return dbName;
    }

    // Hyperium
    public static GameType parse(String mostRecentGameType) {
        mostRecentGameType = mostRecentGameType.toUpperCase();
        try {
            return valueOf(mostRecentGameType);
        } catch (Exception e) {
            return fromDatabase(mostRecentGameType);
        }
    }

    public String getQuestName() {
        if (this == MURDER_MYSTERY) {
            return "murdermystery";
        }

        if (this == QUAKECRAFT) {
            return "quake";
        }

        if (this == SURVIVAL_GAMES) {
            return "hungergames";
        }

        return name().toLowerCase(Locale.ENGLISH).replace("_", "");
    }

    public static GameType fromRealName(String id) {
        for (GameType gameType : VALUES) {
            if (gameType.name.equalsIgnoreCase(id)) {
                return gameType;
            }
        }

        return null;
    }
}
