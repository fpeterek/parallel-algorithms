package org.fpeterek.pa.im

import org.fpeterek.pa.im.graph.Node
import kotlin.random.Random

class IndependentCascade(private val infected: Set<Node>) {

    private val newInfections = mutableSetOf<Node>()

    private fun infectionChance() = Random.nextDouble(1.0);

    private fun getSeedInfluence(seed: Node): Set<Node> = seed.links.asSequence()
        .filter { it.node !in infected && it.node !in newInfections }
        .map {
            when {
                infectionChance() <= it.weight -> getInfluence(seed)
                else -> null
            }
        }
        .filterNotNull()
        .fold(setOf()) { acc, it -> acc + it }

    fun getInfluence(seed: Node): Set<Node> {
        newInfections.add(seed)
        return getSeedInfluence(seed)
    }
}
