package org.fpeterek.pa.im.graph.gen

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Link
import org.fpeterek.pa.im.graph.Node
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

class GraphGenerator private constructor(private val size: Int) {

    companion object {
        fun gen(size: Int) = GraphGenerator(size).genGraph()
    }

    private val linkSet = (0 until size).toMutableSet()

    private fun createNode(id: Int) = Node(id, mutableListOf())

    private fun logRand(upperBound: Double) =
        2.0.pow(Random.nextDouble(log2(upperBound)))

    private fun randomWeight() = round(logRand(1000.0)) / 1000

    // Introduce bias towards lower link counts
    // without completely removing the option of a node which
    // links to every other node in the graph
    private fun randLinkCount() = logRand(linkSet.size - 1.0).roundToInt()

    private fun pickRandom(count: Int, excludeId: Int): List<Int> {

        linkSet.remove(excludeId)
        val links = (0 until count).map {
            val rand = linkSet.random()
            linkSet.remove(rand)
            rand
        }
        linkSet.addAll(links)
        linkSet.add(excludeId)

        return links
    }

    private fun generateLinks(node: Node, all: List<Node>) {
        val count = randLinkCount()
        val links = pickRandom(count, node.id)

        links.forEach {
            node.mutableLinks.add(Link(all[it], randomWeight()))
        }

    }

    private fun genNodes() = (0 until size)
        .map(::createNode)
        .apply {
            onEach {
                generateLinks(it, this)
                it.freeze()
            }
        }

    private fun genGraph() = Graph(genNodes())
}
