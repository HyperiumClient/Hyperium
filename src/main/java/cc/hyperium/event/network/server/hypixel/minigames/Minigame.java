/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.event.network.server.hypixel.minigames;

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

    public final String scoreName;
    public final String dbName;
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
