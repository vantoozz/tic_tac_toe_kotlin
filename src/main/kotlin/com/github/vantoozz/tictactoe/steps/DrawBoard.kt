package com.github.vantoozz.tictactoe.steps

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State

internal class DrawBoard : AbstractStep() {
    override fun doRun(state: State): State {
        state.positions().forEach {
            print(
                when (state.figures[it]) {
                    Figure.X -> "X"
                    Figure.O -> "O"
                    else -> " "
                }
            )

            if (it.x != state.boardSize - 1) {
                print("|")
                return@forEach
            }

            print("\t${(state.boardSize - it.y) * (state.boardSize + it.x)}")

            if (it.y != 1 - state.boardSize) {
                print("\n" + "-+".repeat(state.boardSize - 1) + "-" + "+-".repeat(state.boardSize - 1))
            }

            print("\n")
        }

        return state
    }

}
