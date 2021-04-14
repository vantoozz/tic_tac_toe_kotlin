package com.github.vantoozz.tictactoe

import com.github.vantoozz.tictactoe.appraiser.LinesAppraiser
import com.github.vantoozz.tictactoe.rules.SimpleRules
import com.github.vantoozz.tictactoe.steps.DrawBoard
import com.github.vantoozz.tictactoe.steps.DrawEndGame
import com.github.vantoozz.tictactoe.steps.MakeTurn
import com.github.vantoozz.tictactoe.steps.Step
import com.github.vantoozz.tictactoe.turn.MinimaxTurn
import com.github.vantoozz.tictactoe.turn.UserInputTurn


fun main() {
    DrawEndGame().then(DrawBoard())
        .run(
            tick(
                State(SimpleRules(), emptyMap(), Figure.X, boardSize = 2),
                DrawBoard()
                    .then(MakeTurn(Figure.X, UserInputTurn()))
                    .then(DrawBoard())
                    .then(MakeTurn(Figure.O, MinimaxTurn(LinesAppraiser(SimpleRules()),3)))
            )
        )
}

private tailrec fun tick(state: State, step: Step): State {

    val updatedState = step.run(state)

    if (updatedState.result != null) {
        return updatedState
    }

    return tick(updatedState, step)
}



