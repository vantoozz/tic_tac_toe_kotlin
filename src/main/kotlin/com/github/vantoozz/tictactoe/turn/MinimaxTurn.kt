package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.appraiser.Appraiser
import com.github.vantoozz.tictactoe.exceptions.AppException

//5-9-3-7
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
                forPlayer -> {
                    var alphaValue = alpha
                    var maxVal: Double? = null
                    for (position in state.freePositions) {
                        val value = minimax(state.withTurnMade(position), forPlayer, depth - 1, alphaValue, beta)
                        maxVal = maxVal?.let { maxOf(value, it) } ?: value
                        alphaValue = alphaValue?.let { maxOf(value, it) } ?: value
                        if (beta == null) {
                            continue
                        }
                        if (beta <= alphaValue) {
                            break
                        }
                    }
                    maxVal ?: appraiser.appraise(state, forPlayer)
                }
                else -> {
                    var betaValue = beta
                    var minVal: Double? = null
                    for (position in state.freePositions) {
                        val value = minimax(state.withTurnMade(position), forPlayer, depth - 1, alpha, betaValue)
                        minVal = minVal?.let { minOf(value, it) } ?: value
                        betaValue = betaValue?.let { minOf(value, it) } ?: value
                        if (alpha == null) {
                            continue
                        }
                        if (betaValue <= alpha) {
                            break
                        }
                    }
                    minVal ?: appraiser.appraise(state, forPlayer)
                }
            }
        } ?: throw AppException("Invalid state")
    }
}
