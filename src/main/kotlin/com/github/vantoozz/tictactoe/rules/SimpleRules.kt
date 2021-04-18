package com.github.vantoozz.tictactoe.rules

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.Result
import com.github.vantoozz.tictactoe.exceptions.AppException
import com.github.vantoozz.tictactoe.lines.LinesFinder
import kotlin.math.max
import kotlin.math.pow

internal class SimpleRules(private val linesFinder: LinesFinder) : Rules {

    override fun findResult(figures: Map<Position, Figure?>, boardSize: Int): Result? {
        if (boardSize < 2) {
            return null
        }

        val winLength = 5

        if (figures.size < winLength) {
            return null
        }

        val winners = linesFinder.find(figures)
            .filter { it.length == winLength }
            .map { it.figure }
            .toSet()

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
}
