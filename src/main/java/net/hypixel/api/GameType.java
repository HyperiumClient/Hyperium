/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.hypixel.api;


import java.util.Arrays;

public enum GameType {
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
    UNKNOWN("Unknown", "ERROR", -1),
    BEDWARS("Bedwars", "Bedwars", 57),
    MURDER_MYSTERY("Murder Mystery", "MurderMystery", 59),
    BUILD_BATTLE("Build Battle", "BuildBattle", 60),
    DUELS("Duels", "Duels", 61),
    THE_PIT("The Pit", "PIT", 101);

    private static final GameType[] v = values();

    private final String name;
    private final String dbName;
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
        return Arrays.stream(v).filter(gameType -> gameType.id == id).findFirst().orElse(null);
    }

    public static GameType fromRealName(String id) {
        return Arrays.stream(v).filter(gameType -> gameType.name.equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    /**
     * @param dbName The key used in the database
     * @return The GameType associated with that key, or null if there isn't one.
     */
    public static GameType fromDatabase(String dbName) {
        return Arrays.stream(v).filter(gameType -> gameType.dbName.equals(dbName)).findFirst().orElse(GameType.UNKNOWN);
    }

    public static GameType parse(String mostRecentGameType) {
        mostRecentGameType = mostRecentGameType.toUpperCase();
        try {
            return valueOf(mostRecentGameType);
        } catch (Exception e) {
            return fromDatabase(mostRecentGameType);
        }
    }

    public String getQuestName() {
        return this == MURDER_MYSTERY ? "murdermystery" :
            this == QUAKECRAFT ? "quake" :
                this == SURVIVAL_GAMES ? "hungergames" :
                    name().toLowerCase().replace("_", "");
    }

    public String getDbName() {
        return dbName;
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
}
