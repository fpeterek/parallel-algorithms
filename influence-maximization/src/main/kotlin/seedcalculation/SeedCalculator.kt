package org.fpeterek.pa.im.seedcalculation

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Node
import org.fpeterek.pa.im.infectionmodels.IndependentCascade
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

    private class CalculatorThread(
        private val inQueue: LinkedBlockingQueue<InputMessage>,
        private val outQueue: LinkedBlockingQueue<ThreadResult>,
        private val infected: Set<Node>) {

        var bestSeed: Node? = null
        var highestInfluence: Set<Node> = emptySet()

        private fun pollQueue() = inQueue.take()!!

        private fun getInfluence(node: Node) {
            val influence = IndependentCascade(infected).getInfluence(node)

            if (influence.size > highestInfluence.size) {
                bestSeed = node
                highestInfluence = influence
            }
        }

        private fun sendResult() {
            outQueue.put(ThreadResult(bestSeed, highestInfluence))
        }

        private fun reset() {
            bestSeed = null
            highestInfluence = emptySet()
        }

        private fun handleMessage(inputMessage: InputMessage) = when (inputMessage) {
            is InputMessage.End -> false
            // No, I'm not sure about this either
            // It's possible for one thread to read the GetResult message twice
            // whilst another thread does not read it at all
            is InputMessage.GetResult -> {
                sendResult()
                reset()
                true
            }
            is InputMessage.NodeInfluence -> {
                getInfluence(inputMessage.node)
                true
            }
        }

        fun threadLoop() {
            while (handleMessage(pollQueue()));
        }

    }


    private fun filteredNodes() = graph.nodes.asSequence()
        .filter { it !in infected }

    private fun serialIteration() {
        var bestSeed: Node? = null
        var highestInfluence: Set<Node> = emptySet()

        filteredNodes()
            .forEach {
                val influence = IndependentCascade(infected).getInfluence(it)
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

    private fun parallelIteration() {

        filteredNodes().forEach {
            inQueue.add(InputMessage.NodeInfluence(it))
        }
        threads.forEach { _ ->
            inQueue.add(InputMessage.GetResult)
        }

        val best = outQueue.maxByOrNull { it.infections.size }

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

    private fun shutdownThreads() = threads.forEach { _ ->
        inQueue.add(InputMessage.End)
    }

    private fun getSeeds(): Set<Node> {
        initThreads()
        when {
            threadCount < 2 -> obtainSeeds(this::serialIteration)
            else -> obtainSeeds(this::parallelIteration)
        }
        shutdownThreads()
        return bestSeeds
    }

}
