package org.fpeterek.pa.im

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Node

class SeedCalculator private constructor(private val graph: Graph, private val seeds: Int) {

    companion object {
        fun getSeeds(graph: Graph, seeds: Int) = SeedCalculator(graph, seeds).getSeeds()
    }

    private val infected = mutableSetOf<Node>()
    private val bestSeeds = mutableSetOf<Node>()

    private fun runIteration() {
        var bestSeed: Node? = null
        var highestInfluence: Set<Node> = emptySet()

        graph.nodes.asSequence()
            .filter { it !in infected }
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

    private fun obtainSeeds() {
        while (bestSeeds.size < seeds && infected.size < graph.nodes.size) {
            runIteration()
            println(bestSeeds.size)
            println(infected.size)
        }
    }

    private fun getSeeds(): Set<Node> {
        obtainSeeds()
        return bestSeeds
    }

}
