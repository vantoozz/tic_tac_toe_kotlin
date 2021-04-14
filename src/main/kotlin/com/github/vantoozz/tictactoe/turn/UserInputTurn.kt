package com.github.vantoozz.tictactoe.turn

import com.github.vantoozz.tictactoe.Position
import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.exceptions.AppException
import kotlin.math.ceil
import kotlin.math.pow

internal class UserInputTurn : TurnHandler {
    override fun makeTurn(state: State): Position {
        while (true) try {
            val input = readLine() ?: throw AppException("No input")

            val cellNumber = try {
                input.toInt()
            } catch (e: Exception) {
                throw AppException("Not a number")
            }

            val sideSide = state.boardSize * 2 - 1

            val maxCellNumber = sideSide.toDouble().pow(2)

            if (cellNumber < 1 || cellNumber > maxCellNumber) {
                throw AppException("Position must be between 1 and $maxCellNumber")
            }


            val position = com.github.vantoozz.tictactoe.Position(
                x = (((cellNumber).rem(sideSide)).takeIf { it > 0 } ?: sideSide) - state.boardSize,
                y = state.boardSize - ceil((cellNumber) / sideSide.toDouble()).toInt()
            )

            if (state.figures.containsKey(position)) {
                throw AppException("Position already taken")
            }

            println(position)
            return position
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
