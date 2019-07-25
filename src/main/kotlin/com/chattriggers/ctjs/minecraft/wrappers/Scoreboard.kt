package com.chattriggers.ctjs.minecraft.wrappers

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiIngame
import net.minecraft.scoreboard.ScorePlayerTeam

object Scoreboard {
    private var needsUpdate = true
    private var scoreboardNames = mutableListOf<Score>()
    private var scoreboardTitle = ""

    /**
     * Alias for [Scoreboard.getTitle].
     *
     * @return the scoreboard title
     */
    @JvmStatic
    fun getScoreboardTitle(): String = getTitle()

    /**
     * Gets the top-most string which is displayed on the scoreboard. (doesn't have a score on the side).<br></br>
     * Be aware that this can contain color codes.
     *
     * @return the scoreboard title
     */
    @JvmStatic
    fun getTitle(): String {
        if (needsUpdate) {
            updateNames()
            needsUpdate = false
        }

        return scoreboardTitle
    }

    /**
     * Get all currently visible strings on the scoreboard. (excluding title)<br></br>
     * Be aware that this can contain color codes.
     *
     * @return the list of lines
     */
    @JvmStatic
    fun getLines(): List<Score> {
        // the array will only be updated upon request
        if (needsUpdate) {
            updateNames()
            needsUpdate = false
        }

        return scoreboardNames
    }

    /**
     * Gets the line at the specified index (0 based)
     * Equivalent to Scoreboard.getLines().get(index)
     *
     * @param index the line index
     * @return the score object at the index
     */
    @JvmStatic
    fun getLineByIndex(index: Int): Score = getLines()[index]

    /**
     * Gets a list of lines that have a certain score,
     * i.e. the numbers shown on the right
     *
     * @param score the score to look for
     * @return a list of actual score objects
     */
    @JvmStatic
    fun getLinesByScore(score: Int): List<Score> = getLines().filter {
        it.getPoints() == score
    }

    /**
     * Sets a line in the scoreboard to the specified name and score.
     *
     * @param score the score value for this item
     * @param line the string to display on said line
     * @param override whether or not to remove old lines with the same score
     */
    @JvmStatic
    fun setLine(score: Int, line: String, override: Boolean) {
        try {
            val scoreboard = World.getWorld()?.scoreboard ?: return

            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1)

            val scores = scoreboard.getSortedScores(sidebarObjective)

            if (override) {
                scores.filter {
                    it.scorePoints == score
                }.forEach {
                    scoreboard.removeObjectiveFromEntity(it.playerName, sidebarObjective)
                }
            }
            val theScore = scoreboard.getValueFromObjective(line, sidebarObjective)

            theScore.scorePoints = score
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun setShouldRender(shouldRender: Boolean) {
        HyperiumGuiIngame.renderScoreboard = shouldRender
    }

    @JvmStatic
    fun getShouldRender() = HyperiumGuiIngame.renderScoreboard

    private fun updateNames() {
        scoreboardNames.clear()
        scoreboardTitle = ""

        try {
            val scoreboard = World.getWorld()?.scoreboard ?: return
            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1)
            scoreboardTitle = sidebarObjective!!.displayName

            val scores = scoreboard.getSortedScores(sidebarObjective)

            scoreboardNames = scores.map {
                Score(it)
            }.toMutableList()
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun resetCache() {
        needsUpdate = true
    }

    class Score(val score: net.minecraft.scoreboard.Score) {
        /**
         * Gets the score point value for this score,
         * i.e. the number on the right of the board
         *
         * @return the actual point value
         */
        fun getPoints(): Int = score.scorePoints

        /**
         * Gets the display string of this score
         *
         * @return the display name
         */
        fun getName(): String = ScorePlayerTeam.formatPlayerName(
            World.getWorld()!!.scoreboard.getPlayersTeam(score.playerName),
            score.playerName
        )

        override fun toString(): String = getName()
    }
}
