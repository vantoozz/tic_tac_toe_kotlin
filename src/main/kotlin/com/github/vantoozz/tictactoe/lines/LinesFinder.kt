package com.github.vantoozz.tictactoe.lines

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position

internal interface LinesFinder {
    fun find(figures: Map<Position, Figure?>): Set<Line>
}

internal data class Line(
    val figure: Figure,
    val length: Int
)
