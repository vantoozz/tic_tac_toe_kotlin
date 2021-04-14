package com.github.vantoozz.tictactoe.lines

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.Position

internal class SimpleLinesFinder : LinesFinder {

    override fun find(figures: Map<Position, Figure?>): Set<Line> {
        val yRange = (figures.maxOfOrNull { it.key.y } ?: 0) downTo (figures.minOfOrNull { it.key.y } ?: 0)
        val xRange = (figures.minOfOrNull { it.key.x } ?: 0)..(figures.maxOfOrNull { it.key.x } ?: 0)

        val height = yRange.first - yRange.last + 1
        val width = xRange.last - xRange.first + 1

        if (width == 1 && height == 1) {
            return emptySet()
        }

        val size = maxOf(height, width)

        val examined = mutableSetOf<Position>()
        val lines = mutableSetOf<Line>()

        for (y in yRange) {
            for (x in xRange) {
                val position = Position(x, y)

                if (examined.contains(position)) {
                    continue
                }

                val result = examine(position, figures, size)

                examined.addAll(result.first)
                lines.addAll(result.second)
            }
        }

        return lines
    }

    private fun examine(
        position: Position,
        figures: Map<Position, Figure?>,
        size: Int,
    ): Pair<Set<Position>, Set<Line>> {

        val examined = mutableSetOf<Position>()
        val lines = mutableSetOf<Line>()

        examined.add(position)

        val figure = figures[position] ?: return Pair(examined, lines)

        for (rule in linesRules) {
            for (offset in (1..size)) {
                val nextPosition = rule(position, offset)
                if (figures[nextPosition] == figure) {
                    examined.add(position)
                    continue
                }
                if (offset > 1) {
                    lines.add(Line(figure, offset))
                }
                break
            }
        }

        return Pair(examined, lines)
    }


    private val linesRules = arrayOf<(Position, Int) -> Position>(
        { position, offset -> position.copy(x = position.x + offset) },
        { position, offset -> position.copy(y = position.y + offset) },
        { position, offset -> position.copy(x = position.x + offset, y = position.y + offset) },
        { position, offset -> position.copy(x = position.x - offset, y = position.y + offset) },
    )
}
