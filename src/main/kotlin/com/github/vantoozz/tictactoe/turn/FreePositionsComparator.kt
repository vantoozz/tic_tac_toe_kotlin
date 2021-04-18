package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.State

internal class FreePositionsComparator(private val state: State) : Comparator<Position> {
    override fun compare(one: Position, two: Position) =
        freeNeighborsCount(one) - freeNeighborsCount(two)

    private fun freeNeighborsCount(position: Position) =
        state.freePositions.run {
            position.neighbors.count { contains(it) }
        }
}

