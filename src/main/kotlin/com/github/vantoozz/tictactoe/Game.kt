package com.github.vantoozz.tictactoe

import com.github.vantoozz.tictactoe.appraiser.Cached
import com.github.vantoozz.tictactoe.appraiser.LinesAppraiser
import com.github.vantoozz.tictactoe.lines.SimpleLinesFinder
import com.github.vantoozz.tictactoe.rules.SimpleRules
import com.github.vantoozz.tictactoe.steps.DrawBoard
import com.github.vantoozz.tictactoe.steps.DrawEndGame
import com.github.vantoozz.tictactoe.steps.MakeTurn
import com.github.vantoozz.tictactoe.steps.Step
import com.github.vantoozz.tictactoe.turn.MinimaxTurn
import com.github.vantoozz.tictactoe.turn.UserInputTurn


fun main() {
    val appraiser = Cached(LinesAppraiser(SimpleLinesFinder()))
    DrawEndGame().then(DrawBoard())
        .run(
            tick(
                State(SimpleRules(SimpleLinesFinder()), emptyMap(), Figure.X, boardSize = 5),
                DrawBoard()
                    .then(MakeTurn(Figure.X, UserInputTurn()))
                    .then(DrawBoard())
                    .then(MakeTurn(Figure.O, MinimaxTurn(appraiser, 5)))
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



