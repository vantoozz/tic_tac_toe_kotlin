package com.github.vantoozz.tictactoe

class MakeTurn : AbstractStep() {
    override fun doRun(state: State): State {
        state.activePlayer?.let { activePlayer ->
            print("[${activePlayer}] Make a turn:\n")

            with(
                state.withTurnMade(
                    when (activePlayer) {
                        Figure.X -> handleInput(state)
                        Figure.O -> pickBestPosition(state, activePlayer)
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

        } ?: throw RuntimeException("No active player")
    }


    private fun pickBestPosition(state: State, forPlayer: Figure): Int {
        val position = state.freePositions
            .maxByOrNull { minimax(state.withTurnMade(it), forPlayer) }
            ?: throw RuntimeException("Cannot make a turn")

        print("$position\n")

        return position
    }

    private fun minimax(state: State, forPlayer: Figure): Long {

        state.result?.let {
            if (it.draw) {
                return 0
            }

            it.winner?.let { winner ->
                return when (winner) {
                    forPlayer -> 1
                    else -> -1
                }
            }
        }

        state.activePlayer?.let { activePlayer ->
            return when (activePlayer) {
                forPlayer -> state.freePositions.maxOfOrNull { minimax(state.withTurnMade(it), forPlayer) } ?: 0L
                else -> state.freePositions.minOfOrNull { minimax(state.withTurnMade(it), forPlayer) } ?: 0L
            }
        } ?: throw RuntimeException("Invalid state")
    }

    private fun handleInput(state: State): Int {
        while (true) try {
            val input = readLine() ?: throw RuntimeException("No input")

            val position = try {
                input.toInt()
            } catch (e: Exception) {
                throw RuntimeException("Not a number")
            }

            if (position < 1 || position > 9) {
                throw RuntimeException("Position must be between 1 and 9")
            }

            if (state.figures.containsKey(position)) {
                throw RuntimeException("Position already taken")
            }

            return position
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
