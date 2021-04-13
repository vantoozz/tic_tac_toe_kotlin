package com.github.vantoozz.tictactoe

class MakeTurn : AbstractStep() {
    override fun doRun(state: State): State {
        state.activePlayer?.let { activePlayer ->
            print("[${activePlayer}] Make a turn:\n")

            with(
                state.withTurnMade(
                    when (activePlayer) {
                        Figure.X -> handleInput(state)
                        Figure.O -> pickBestPosition(state)
                    }
                )
            ) {
                return this
                    .takeIf { it.result == null }
                    ?.freePositions
                    ?.takeIf { it.count() == 1 }
                    ?.first()
                    ?.let {
                        withTurnMade(it)
                    } ?: this
            }

        } ?: throw AppException("No active player")
    }


    private fun pickRandomPosition(state: State) = state.freePositions.random()

    private fun pickBestPosition(state: State): Position {
        val position = state.pickBestTurn() ?: throw AppException("Cannot make a turn")

        print("$position\n")

        return position
    }
}
