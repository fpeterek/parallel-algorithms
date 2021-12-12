package org.fpeterek.pa.im

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Node

class SeedCalculator(val graph: Graph, val seeds: Int) {

    val infected = mutableSetOf<Node>()
    val bestSeeds = mutableSetOf<Node>()

    fun getSeeds(): Set<Node> {
        while (bestSeeds.size < seeds && infected.size < graph.nodes.size) {
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

        return bestSeeds
    }

}
