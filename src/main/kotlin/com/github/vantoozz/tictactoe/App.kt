package com.github.vantoozz.tictactoe

fun main() {
    DrawBoard()
        .then(DrawEndGame())
        .run(
            tick(
                State(emptyMap(), Figure.X),
                DrawBoard()
                    .then(MakeTurn())
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



