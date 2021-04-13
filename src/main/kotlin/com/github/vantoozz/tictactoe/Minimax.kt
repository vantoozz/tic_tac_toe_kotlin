package com.github.vantoozz.tictactoe

internal fun State.pickBestTurn(): Position? {
    return activePlayer?.let { forPlayer ->
        freePositions.maxByOrNull { minimax(withTurnMade(it), forPlayer, 3) }
    }
}

private fun minimax(state: State, forPlayer: Figure, depth: Int): Double {
    if (depth <= 0) {
        return relativeValue(state, forPlayer)
    }

    state.result?.let {
        if (it.draw) {
            return 0.0
        }

        it.winner?.let { winner ->
            return when (winner) {
                forPlayer -> 1.0
                else -> -1.0
            }
        }
    }

    state.activePlayer?.let { activePlayer ->
        return when (activePlayer) {
            forPlayer -> state.freePositions
                .maxOfOrNull { minimax(state.withTurnMade(it), forPlayer, depth - 1) }
                ?: relativeValue(state, forPlayer)
            else -> state.freePositions
                .minOfOrNull {
                    minimax(state.withTurnMade(it), forPlayer, depth - 1)
                } ?: relativeValue(state, forPlayer)
        }
    } ?: throw AppException("Invalid state")
}

private fun relativeValue(state: State, forPlayer: Figure): Double {
    val playerValue = value(state, forPlayer)
    val opponentValue = value(state, forPlayer.opposite)

    val totalValue = playerValue + opponentValue

    if (totalValue == 0L) {
        return 0.0
    }

    return 2 * playerValue.toDouble() / totalValue.toDouble() - 1
}

private fun value(state: State, forPlayer: Figure): Long {

    val figures = state.figures
        .filter { it.value == forPlayer }
        .map { it.key }
        .toSet()

    val maxLineSize = state.boardSize * 2 - 1

    return figures.sumOf { position ->
        (maxLineSize downTo 2).mapNotNull { lineSize ->
            figures.hasAnyLine(position, lineSize, *lineRules).takeIf { it }?.let { lineSize }
        }.sum()

    }.toLong()
}
