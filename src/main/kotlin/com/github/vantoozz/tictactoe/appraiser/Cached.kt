package com.github.vantoozz.tictactoe.appraiser

import com.github.vantoozz.tictactoe.Figure
import com.github.vantoozz.tictactoe.State
import kotlin.math.round


internal class Cached(private val appraiser: Appraiser) : Appraiser {

    private val cachedFor = Figure.X

    private var hits = 0
    private var misses = 0

    private val cache: MutableMap<State, Double> = mutableMapOf()

    override fun appraise(state: State, forPlayer: Figure) =
        cache[state]?.let {
            hits++
            if (forPlayer == cachedFor) it else -it
        } ?: run {
            misses++
            appraiser.appraise(state, cachedFor)
                .also {
                    cache[state] = it
                    if (cache.size % 10000 == 0) {
                        println("===${round(100 * (hits / (hits + misses).toDouble()))}===")
                        println(cache.size)
                    }
                }
        }
}


