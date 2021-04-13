package com.github.vantoozz.tictactoe

data class State(
    val figures: Map<Position, Figure?>,
    val activePlayer: Figure?,
    val result: Result? = null,
    val boardSize: Int = 2,
) {
    init {
        if (boardSize < 1) {
            throw AppException("Board size must be greater than zero")
        }

        if (result != null && activePlayer != null) {
            throw AppException("Finished game cannot have an active player")
        }
        if (result == null && activePlayer == null) {
            throw AppException("Not finished game must have an active player")
        }

        setOf(
            figures.minByOrNull { it.key.x }?.takeIf { it.key.x <= -boardSize },
            figures.maxByOrNull { it.key.x }?.takeIf { it.key.x >= boardSize },
            figures.minByOrNull { it.key.y }?.takeIf { it.key.y <= -boardSize },
            figures.maxByOrNull { it.key.y }?.takeIf { it.key.y >= boardSize },
        )
            .filterNotNull()
            .firstOrNull()
            ?.let { throw AppException("Position out of the board: $it") }
    }


    fun positions(
        predicate: (Position) -> Boolean = { true }
    ) = with(boardSize - 1) {
        (this downTo -this)
            .map { y ->
                (-this..this)
                    .map { x -> Position(x, y) }
            }
            .flatten()
            .filter { predicate(it) }
            .toList()
    }


    val freePositions: List<Position>
        get() = positions { !figures.containsKey(it) }

    fun withTurnMade(position: Position): State {
        if (figures.containsKey(position)) {
            throw AppException("Position already taken")
        }

        activePlayer?.let { player ->

            val updatedFigures = figures.toMutableMap().also {
                it[position] = player
            }.toMap()

            val updatedResult = isGameOver(updatedFigures, boardSize)

            return copy(
                figures = updatedFigures,
                result = updatedResult,
                activePlayer = if (null == updatedResult) activePlayer.opposite else null
            )

        } ?: throw AppException("Cannot make a turn. No active player")
    }
}

enum class Figure {
    X,
    O;

    val opposite: Figure
        get() = when (this) {
            X -> O
            O -> X
        }
}

data class Position(val x: Int, val y: Int)

data class Result(
    val winner: Figure? = null,
    val draw: Boolean,
) {
    init {
        when (draw) {
            true -> if (winner != null) throw AppException("Drawn game cannot have a winner")
            false -> if (winner == null) throw AppException("Not a drawn game must have a winner")
        }
    }
}
