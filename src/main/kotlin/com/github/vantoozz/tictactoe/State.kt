package com.github.vantoozz.tictactoe

data class State(
    val figures: Map<Int, Figure?>,
    val activePlayer: Figure?,
    val result: Result? = null,
) {
    init {
        if (result != null && activePlayer != null) {
            throw RuntimeException("Finished game cannot have an active player")
        }
        if (result == null && activePlayer == null) {
            throw RuntimeException("Not finished game must have an active player")
        }
    }

    val freePositions: Set<Int>
        get() = (1..9).filterNot { figures.containsKey(it) }.toSet()

    fun withTurnMade(position: Int): State {
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

    private fun checkResult(figures: Map<Int, Figure?>): Result? {
        if (figures.size < 3) {
            return null
        }

        val winners = figures
            .map {
                it.value?.let { figure ->
                    figure to it.key
                }
            }
            .filterNotNull()
            .fold(mutableMapOf<Figure, Set<Int>>()) { carry, pair ->
                carry.also {
                    it[pair.first] = (it[pair.first] ?: emptySet()) + setOf(pair.second)
                }
            }
            .toMap()
            .map {
                it.key to it.value.let { set ->
                    set.containsAll(setOf(1, 2, 3))
                            ||
                            set.containsAll(setOf(4, 5, 6))
                            ||
                            set.containsAll(setOf(7, 8, 9))
                            ||
                            set.containsAll(setOf(1, 4, 7))
                            ||
                            set.containsAll(setOf(2, 5, 8))
                            ||
                            set.containsAll(setOf(3, 6, 9))
                            ||
                            set.containsAll(setOf(1, 5, 9))
                            ||
                            set.containsAll(setOf(3, 5, 7))
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

        if (figures.size == 9) {
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
