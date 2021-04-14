package com.github.vantoozz.tictactoe.appraiser

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State

internal interface Appraiser {
    fun appraise(state: State, forPlayer: Figure): Double
}
