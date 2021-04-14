package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.State

internal class RandomTurn : TurnHandler {
    override fun makeTurn(state: State) = state.freePositions.random()
}
