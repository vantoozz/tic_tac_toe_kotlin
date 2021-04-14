package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.State

internal interface TurnHandler {
    fun makeTurn(state: State): Position
}
