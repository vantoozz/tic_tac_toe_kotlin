package com.github.vantoozz.tictactoe.steps

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.turn.TurnHandler

internal class MakeTurn(
    private val player: Figure,
    private val turnHandler: TurnHandler
) : AbstractStep() {
    override fun doRun(state: State) =
        state.activePlayer
            ?.takeIf { it == player }
            ?.let { activePlayer ->
                print("[${activePlayer}] Make a turn:\n")

                val updatedState = state.withTurnMade(turnHandler.makeTurn(state))

                return updatedState
                    .takeIf { it.result == null }
                    ?.freePositions
                    ?.takeIf { it.count() == 1 }
                    ?.first()
                    ?.let {
                        updatedState.withTurnMade(it)
                    } ?: updatedState

            } ?: state
}
