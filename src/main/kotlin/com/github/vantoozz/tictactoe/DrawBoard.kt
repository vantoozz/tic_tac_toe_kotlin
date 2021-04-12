package com.github.vantoozz.tictactoe

class DrawBoard : AbstractStep() {
    override fun doRun(state: State): State {
        state.positions().forEach {
            print(
                when (state.figures[it]) {
                    Figure.X -> "X"
                    Figure.O -> "O"
                    else -> " "
                }
            )

            if (it.y != state.boardSize - 1) {
                print("|")
                return@forEach
            }

            if (it.x != state.boardSize - 1) {
                print("\n" + "-+".repeat(state.boardSize - 1) + "-" + "+-".repeat(state.boardSize - 1))
            }

            print("\n")
        }

        return state
    }

}
