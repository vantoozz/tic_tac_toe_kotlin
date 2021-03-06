package com.github.vantoozz.tictactoe.steps

import com.github.vantoozz.tictactoe.State

internal interface Step {
    fun run(state: State): State
}

internal abstract class AbstractStep : Step {

    private lateinit var nextStep: AbstractStep

    abstract fun doRun(state: State): State

    fun then(nextStep: AbstractStep): AbstractStep {
        if (!this::nextStep.isInitialized) {
            this.nextStep = nextStep
            return this
        }
        this.nextStep.then(nextStep)
        return this
    }

    override fun run(state: State): State {
        val updatedState = doRun(state)

        if (!this::nextStep.isInitialized) {
            return updatedState
        }

        return nextStep.run(updatedState)
    }
}

