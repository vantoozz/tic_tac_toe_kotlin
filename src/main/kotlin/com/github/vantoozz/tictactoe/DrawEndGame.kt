package com.github.vantoozz.tictactoe

class DrawEndGame : AbstractStep() {
    override fun doRun(state: State): State {
        state.result?.let { result ->
            result.winner?.let {
                print("$it wins!")
            } ?: print("DRAW")
        } ?: throw AppException("No game result")

        return state
    }
}
