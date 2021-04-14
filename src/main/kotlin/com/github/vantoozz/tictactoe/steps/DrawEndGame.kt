package com.github.vantoozz.tictactoe.steps

import com.github.vantoozz.tictactoe.State
import com.github.vantoozz.tictactoe.exceptions.AppException

internal class DrawEndGame : AbstractStep() {
    override fun doRun(state: State): State {
        state.result?.let { result ->
            result.winner?.let {
                print("$it wins!\n")
            } ?: print("DRAW\n")
        } ?: throw AppException("No game result")

        return state
    }
}
