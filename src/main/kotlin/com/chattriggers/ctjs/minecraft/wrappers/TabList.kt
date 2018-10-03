package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.GameType
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import java.util.ArrayList
import java.util.Comparator

@External
object TabList {
    /**
     * Gets names set in scoreboard objectives
     *
     * @return The formatted names
     */
    @JvmStatic
    fun getNamesByObjectives(): List<String> {
        return try {
            val scoreboard = World.getWorld()!!.scoreboard
            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(0)
            val scores = scoreboard.getSortedScores(sidebarObjective)

            scores.map {
                val team = scoreboard.getPlayersTeam(it.playerName)
                ScorePlayerTeam.formatPlayerName(team, it.playerName)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    /**
     * Gets all names in tabs without formatting
     *
     * @return the unformatted names
     */
    @JvmStatic
    fun getNames(): List<String> {
        if (Player.getPlayer() == null) return listOf()

        val tab = Ordering.from(PlayerComparator())
        val conn = Client.getConnection()
        val list = tab.sortedCopy(conn.playerInfoMap)

        return list.map {
            it.gameProfile.name
        }
    }

    internal class PlayerComparator internal constructor() : Comparator<NetworkPlayerInfo> {
        override fun compare(playerOne: NetworkPlayerInfo, playerTwo: NetworkPlayerInfo): Int {
            val teamOne = playerOne.playerTeam
            val teamTwo = playerTwo.playerTeam

            return ComparisonChain
                    .start()
                    .compareTrueFirst(
                            playerOne.gameType != GameType.SPECTATOR,
                            playerTwo.gameType != GameType.SPECTATOR
                    ).compare(
                            //#if MC<=10809
                            teamOne?.registeredName ?: "",
                            teamTwo?.registeredName ?: ""
                            //#else
                            //$$ teamOne?.name ?: "",
                            //$$ teamTwo?.name ?: ""
                            //#endif
                    ).compare(
                            playerOne.gameProfile.name,
                            playerTwo.gameProfile.name
                    ).result()
        }
    }
}