package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.appraiser.Appraiser
import com.github.vantoozz.tictactoe.exceptions.AppException

internal class MinimaxTurn(
    private val appraiser: Appraiser,
    private val depth: Int
) : TurnHandler {

    override fun makeTurn(state: State): Position {

        val position = state.activePlayer?.let { forPlayer ->
            state.freePositions.maxByOrNull { minimax(state.withTurnMade(it), forPlayer, depth) }
        } ?: throw AppException("Cannot make a turn")

        print("$position\n")

        return position
    }

    private fun minimax(
        state: State,
        forPlayer: Figure,
        depth: Int,
        alpha: Double? = null,
        beta: Double? = null
    ): Double {
        if (depth <= 0) {
            return appraiser.appraise(state, forPlayer)
        }

        state.result?.let {
            if (it.draw) {
                return 0.0
            }

            it.winner?.let { winner ->
                return when (winner) {
                    forPlayer -> 1.0
                    else -> -1.0
                }
            }
        }

        state.activePlayer?.let { activePlayer ->
            return when (activePlayer) {
                forPlayer -> maximize(state, forPlayer, depth, alpha, beta)
                else -> minimize(state, forPlayer, depth, alpha, beta)
            }
        } ?: throw AppException("Invalid state")
    }

    private fun minimize(
        state: State,
        forPlayer: Figure,
        depth: Int,
        alpha: Double?,
        beta: Double?
    ): Double {
        var betaVar = beta
        var minVar: Double? = null
        for (position in state.freePositions) {
            val value = minimax(state.withTurnMade(position), forPlayer, depth - 1, alpha, betaVar)
            minVar = minVar?.let { minOf(value, it) } ?: value
            betaVar = betaVar?.let { minOf(value, it) } ?: value
            if (alpha == null) {
                continue
            }
            if (betaVar <= alpha) {
                break
            }
        }
        return minVar ?: appraiser.appraise(state, forPlayer)
    }

    private fun maximize(
        state: State,
        forPlayer: Figure,
        depth: Int,
        alpha: Double?,
        beta: Double?
    ): Double {
        var alphaVar = alpha
        var maxVar: Double? = null
        for (position in state.freePositions) {
            val value = minimax(state.withTurnMade(position), forPlayer, depth - 1, alphaVar, beta)
            maxVar = maxVar?.let { maxOf(value, it) } ?: value
            alphaVar = alphaVar?.let { maxOf(value, it) } ?: value
            if (beta == null) {
                continue
            }
            if (beta <= alphaVar) {
                break
            }
        }
        return maxVar ?: appraiser.appraise(state, forPlayer)
    }
}
