package com.github.vantoozz.tictactoe

data class State(
    val figures: Map<Position, Figure?>,
    val activePlayer: Figure?,
    val result: Result? = null,
    val boardSize: Int = 5,
) {
    init {
        if (boardSize < 1) {
            throw RuntimeException("Board size must be greater than zero")
        }

        if (result != null && activePlayer != null) {
            throw RuntimeException("Finished game cannot have an active player")
        }
        if (result == null && activePlayer == null) {
            throw RuntimeException("Not finished game must have an active player")
        }

        setOf(
            figures.minByOrNull { it.key.x }?.takeIf { it.key.x <= -boardSize },
            figures.maxByOrNull { it.key.x }?.takeIf { it.key.x >= boardSize },
            figures.minByOrNull { it.key.y }?.takeIf { it.key.y <= -boardSize },
            figures.maxByOrNull { it.key.y }?.takeIf { it.key.y >= boardSize },
        )
            .filterNotNull()
            .firstOrNull()
            ?.let { throw RuntimeException("Position out of the board: $it") }
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
            throw RuntimeException("Position already taken")
        }

        activePlayer?.let { player ->

            val updatedFigures = figures.toMutableMap().also {
                it[position] = player
            }.toMap()

            val updatedResult = checkResult(updatedFigures)

            return copy(
                figures = updatedFigures,
                result = updatedResult,
                activePlayer = if (null == updatedResult) activePlayer.opposite else null
            )

        } ?: throw RuntimeException("Cannot make a turn. No active player")
    }

    private fun checkResult(figures: Map<Position, Figure?>): Result? {
        if (boardSize < 2) {
            return null
        }

        val rowSize = boardSize * 2 - 1

        if (figures.size < rowSize) {
            return null
        }

        val winners = figures
            .map {
                it.value?.let { figure ->
                    figure to it.key
                }
            }
            .filterNotNull()
            .fold(mutableMapOf<Figure, Set<Position>>()) { carry, pair ->
                carry.also {
                    it[pair.first] = (it[pair.first] ?: emptySet()) + setOf(pair.second)
                }
            }
            .toMap()
            .map { entry ->
                entry.key to entry.value
                    .let { figures ->
                        val minX = figures.minOfOrNull { it.x } ?: -(boardSize - 1)
                        val minY = figures.minOfOrNull { it.y } ?: -(boardSize - 1)
                        (minY until boardSize).map { y ->
                            (minX until boardSize).map { x ->
                                entry.value.hasAnyRow(
                                    Position(x, y),
                                    rowSize,
                                    { position, offset -> position.copy(x = x + offset) },
                                    { position, offset -> position.copy(y = y + offset) },
                                    { position, offset -> position.copy(x = x + offset, y = y + offset) },
                                    { position, offset -> position.copy(x = x - offset, y = y + offset) },
                                )
                            }
                        }.flatten().count { it } > 0
                    }
            }
            .filter { it.second }
            .map { it.first }

        if (winners.count() > 1) {
            throw RuntimeException("More than one winner")
        }

        if (winners.count() == 1) {
            return Result(draw = false, winner = winners.first())
        }

        if (freePositions.isEmpty()) {
            return Result(draw = true)
        }

        return null
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

private fun Set<Position>.hasAnyRow(
    position: Position,
    rowSize: Int,
    vararg rules: (Position, Int) -> Position
) =
    rules.firstOrNull { hasRow(position, rowSize, it) } != null

private fun Set<Position>.hasRow(
    position: Position,
    rowSize: Int,
    nextPositionFn: (Position, Int) -> Position
) = (0 until rowSize)
    .map { nextPositionFn(position, it) }
    .map { contains(it) }
    .count { it } == rowSize

data class Position(val x: Int, val y: Int)

data class Result(
    val winner: Figure? = null,
    val draw: Boolean,
) {
    init {
        when (draw) {
            true -> if (winner != null) throw RuntimeException("Drawn game cannot have a winner")
            false -> if (winner == null) throw RuntimeException("Not a drawn game must have a winner")
        }
    }
}
