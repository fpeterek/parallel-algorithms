package org.fpeterek.pa.im.infectionmodels

import org.fpeterek.pa.im.graph.Node
import java.util.*
import kotlin.random.Random

class IndependentCascade(private val infected: Set<Node>) {

    private val newInfections = mutableSetOf<Node>()
    private val queue: Queue<Node> = LinkedList()

    private fun infectionChance() = Random.nextDouble(1.0)

    private fun addInfection(node: Node) {
        newInfections.add(node)
        queue.add(node)
    }

    private fun runIteration() = queue.poll().links.asSequence()
        .filter { it.node !in infected && it.node !in newInfections }
        .filter { infectionChance() <= it.weight }
        .forEach { addInfection(it.node) }

    fun getInfluence(seed: Node): Set<Node> {
        addInfection(seed)

        while (queue.isNotEmpty()) {
            runIteration()
        }

        return newInfections
    }
}
