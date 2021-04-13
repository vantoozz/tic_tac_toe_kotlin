package com.github.vantoozz.tictactoe

import kotlin.math.pow

fun isGameOver(figures: Map<Position, Figure?>, boardSize: Int): Result? {
    if (boardSize < 2) {
        return null
    }

    val lineSize = boardSize * 2 - 1

    if (figures.size < lineSize) {
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
                            entry.value.hasAnyLine(
                                Position(x, y),
                                lineSize,
                                *lineRules
                            )
                        }
                    }.flatten().count { it } > 0
                }
        }
        .filter { it.second }
        .map { it.first }

    if (winners.count() > 1) {
        throw AppException("More than one winner")
    }

    if (winners.count() == 1) {
        return Result(draw = false, winner = winners.first())
    }

    if (figures.count() >= (boardSize * 2 - 1).toDouble().pow(2)) {
        return Result(draw = true)
    }

    return null
}


val lineRules = arrayOf<(Position, Int) -> Position>(
    { position, offset -> position.copy(x = position.x + offset) },
    { position, offset -> position.copy(y = position.y + offset) },
    { position, offset -> position.copy(x = position.x + offset, y = position.y + offset) },
    { position, offset -> position.copy(x = position.x - offset, y = position.y + offset) },
)
