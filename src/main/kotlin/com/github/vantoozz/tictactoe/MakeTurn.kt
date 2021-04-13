package com.github.vantoozz.tictactoe

import kotlin.math.ceil
import kotlin.math.pow

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


    private fun pickRandomPosition(state: State) = state.freePositions.random()

    private fun pickBestPosition(state: State, forPlayer: Figure): Position {
        val position = state.freePositions
            .maxByOrNull { minimax(state.withTurnMade(it), forPlayer, 3) }
            ?: throw RuntimeException("Cannot make a turn")

        print("$position\n")

        return position
    }

    private fun minimax(state: State, forPlayer: Figure, depth: Int): Long {
        if (depth <= 0) {
            return 0
        }

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
                forPlayer -> state.freePositions
                    .maxOfOrNull { minimax(state.withTurnMade(it), forPlayer, depth - 1) }
                    ?: 0L
                else -> state.freePositions
                    .minOfOrNull {
                        minimax(state.withTurnMade(it), forPlayer, depth - 1)
                    } ?: 0L
            }
        } ?: throw RuntimeException("Invalid state")
    }

    private fun handleInput(state: State): Position {
        while (true) try {
            val input = readLine() ?: throw RuntimeException("No input")

            val cellNumber = try {
                input.toInt()
            } catch (e: Exception) {
                throw RuntimeException("Not a number")
            }

            val sideSide = state.boardSize * 2 - 1

            val maxCellNumber = sideSide.toDouble().pow(2)

            if (cellNumber < 1 || cellNumber > maxCellNumber) {
                throw RuntimeException("Position must be between 1 and $maxCellNumber")
            }


            val position = Position(
                x = (((cellNumber).rem(sideSide)).takeIf { it > 0 } ?: sideSide) - state.boardSize,
                y = state.boardSize - ceil((cellNumber) / sideSide.toDouble()).toInt()
            )

            if (state.figures.containsKey(position)) {
                throw RuntimeException("Position already taken")
            }

            println(position)
            return position
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
