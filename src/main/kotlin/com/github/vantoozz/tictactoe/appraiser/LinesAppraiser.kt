package com.github.vantoozz.tictactoe.appraiser

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.hasAnyLine
import com.github.vantoozz.tictactoe.rules.Rules

internal class LinesAppraiser(private val rules: Rules) : Appraiser {

    override fun appraise(state: State, forPlayer: Figure): Double {
        val playerValue = value(state, forPlayer)
        val opponentValue = value(state, forPlayer.opposite)

        val totalValue = playerValue + opponentValue

        if (totalValue == 0L) {
            return 0.0
        }

        return 2 * playerValue.toDouble() / totalValue.toDouble() - 1
    }

    private fun value(state: State, forPlayer: Figure): Long {

        val figures = state.figures
            .filter { it.value == forPlayer }
            .map { it.key }
            .toSet()

        val maxLineSize = state.boardSize * 2 - 1

        return figures.sumOf { position ->
            (maxLineSize downTo 2).mapNotNull { lineSize ->
                figures.hasAnyLine(position, lineSize, *rules.linesRules()).takeIf { it }?.let { lineSize }
            }.sum()

        }.toLong()
    }
}
