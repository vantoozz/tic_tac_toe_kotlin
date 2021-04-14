package com.github.vantoozz.tictactoe.appraiser

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.lines.LinesFinder

internal class LinesAppraiser(
    private val linesFinder: LinesFinder
) : Appraiser {
    override fun appraise(state: State, forPlayer: Figure): Double {
        val values = with(
            linesFinder.find(state.figures).partition { it.figure == forPlayer }
        ) {
            Pair(
                first.sumOf { it.length },
                second.sumOf { it.length }
            )
        }

        val playerValue = values.first
        val opponentValue = values.second

        val totalValue = playerValue + opponentValue

        if (totalValue == 0) {
            return 0.0
        }

        return 2 * playerValue.toDouble() / totalValue.toDouble() - 1
    }
}
