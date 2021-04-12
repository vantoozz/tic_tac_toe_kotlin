package com.github.vantoozz.tictactoe

class DrawBoard : AbstractStep() {
    override fun doRun(state: State): State {
        (1..9).forEach {
            print(
                when (state.figures[it]) {
                    Figure.X -> "X"
                    Figure.O -> "O"
                    else -> " "
                }
            )
            if (0 != it % 3) {
                print("|")
                return@forEach
            }
            if (0 == it % 3) {
                print("\n")
            }
            if (9 != it) {
                print("-+-+-\n")
            }
        }

        return state
    }

}
