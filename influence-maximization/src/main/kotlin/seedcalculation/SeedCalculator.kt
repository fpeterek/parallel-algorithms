package org.fpeterek.pa.im.seedcalculation

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Node
import org.fpeterek.pa.im.infectionmodels.IndependentCascade
import seedcalculation.CalculatorThread
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


class SeedCalculator private constructor(
    private val graph: Graph,
    private val seeds: Int,
    private val threadCount: Int) {

    companion object {
        fun getSeeds(graph: Graph, seeds: Int, threads: Int) =
            SeedCalculator(graph, seeds, threads).getSeeds()
    }

    private val inQueue = LinkedBlockingQueue<InputMessage>()
    private val outQueue = LinkedBlockingQueue<ThreadResult>()
    private val threads = mutableListOf<Thread>()
    private val infected = mutableSetOf<Node>()
    private val bestSeeds = mutableSetOf<Node>()

    private fun filteredNodes() = graph.nodes.asSequence()
        .filter { it !in infected }

    private fun serialIteration() {
        var bestSeed: Node? = null
        var highestInfluence: Set<Node> = emptySet()

        filteredNodes()
            .forEach {
                val influence = IndependentCascade(it, infected)
                if (influence.size > highestInfluence.size) {
                    bestSeed = it
                    highestInfluence = influence
                }
            }

        if (bestSeed != null) {
            bestSeeds.add(bestSeed!!)
            infected.addAll(highestInfluence)
        }
    }

    private fun withThreads(fn: () -> Unit) {
        initThreads()
        fn()
        shutdownThreads()
    }

    private fun parallelIteration() {

        withThreads {
            filteredNodes().forEach {
                inQueue.add(InputMessage.NodeInfluence(it))
            }
        }

        val best = (0 until threadCount)
            .map { outQueue.take() }
            .maxByOrNull { it.infections.size }

        if (best?.seed != null) {
            bestSeeds.add(best.seed)
            infected.addAll(best.infections)
        }
    }

    private fun obtainSeeds(iteration: () -> Unit) {
        while (bestSeeds.size < seeds && infected.size < graph.nodes.size) {
            iteration()
        }
    }

    private fun createThread() = thread {
        CalculatorThread(inQueue, outQueue, infected).threadLoop()
    }

    private fun initThreads() = (0 until threadCount).forEach { _ ->
        threads.add(createThread())
    }

    private fun shutdownThreads() = threads
        .onEach { inQueue.add(InputMessage.End) }
        .clear()

    private fun getSeeds(): Set<Node> {
        when {
            threadCount < 2 -> obtainSeeds(this::serialIteration)
            else -> obtainSeeds(this::parallelIteration)
        }
        return bestSeeds
    }
}
