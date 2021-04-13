package com.github.vantoozz.tictactoe

internal fun Set<Position>.hasAnyLine(
    position: Position,
    lineSize: Int,
    vararg rules: (Position, Int) -> Position
) =
    rules.firstOrNull { hasLine(position, lineSize, it) } != null

private fun Set<Position>.hasLine(
    position: Position,
    lineSize: Int,
    nextPositionFn: (Position, Int) -> Position
) = (0 until lineSize)
    .map { nextPositionFn(position, it) }
    .map { contains(it) }
    .count { it } == lineSize
