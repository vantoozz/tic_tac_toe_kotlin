package com.github.vantoozz.tictactoe

class MakeTurn : AbstractStep() {
    override fun doRun(state: State): State {
        while (true) try {
            state.activePlayer?.let { activePlayer ->
                print("[${activePlayer}] Make a turn:\n")

                val updatedState = state.withTurnMade(
                    when (activePlayer) {
                        Figure.X -> handleInput(state)
                        Figure.O -> pickPosition(state, activePlayer)
                    }
                )

                updatedState
                    .takeIf { it.result == null }
                    ?.freePosition
                    ?.takeIf { it.count() == 1 }
                    ?.first()
                    ?.let {
                        return updatedState.withTurnMade(it)
                    }

                return updatedState

            } ?: throw RuntimeException("No active player")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun pickPosition(state: State, forPlayer: Figure): Int {
        val position = state.freePosition
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
                forPlayer -> state.freePosition.maxOfOrNull { minimax(state.withTurnMade(it), forPlayer) } ?: 0L
                else -> state.freePosition.minOfOrNull { minimax(state.withTurnMade(it), forPlayer) } ?: 0L
            }
        } ?: throw RuntimeException("Invalid state")
    }

    private fun handleInput(state: State): Int {
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
    }

}
