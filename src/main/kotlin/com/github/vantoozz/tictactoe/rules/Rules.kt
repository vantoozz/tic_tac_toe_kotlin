package com.github.vantoozz.tictactoe.rules

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.Result

internal interface Rules {

    fun findResult(figures: Map<Position, Figure?>, boardSize: Int): Result?

    fun linesRules(): Array<(Position, Int) -> Position>
}
