package org.fpeterek.pa.im.graph.gen

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Link
import org.fpeterek.pa.im.graph.Node
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

object GraphGenerator {

    private class Generator(val size: Int) {
        val linkSet = (0 until size).toMutableSet()

        fun createNode(id: Int) = Node(id, mutableListOf())

        fun randomWeight() = round(Random.nextDouble(1.0) * 1000) / 1000

        // Introduce bias towards lower link counts
        // without completely removing the option of a node which
        // links to every other node in the graph
        fun randLinkCount() = round(2.0.pow(Random.nextDouble(log2(linkSet.size - 1.0)))).toInt()

        fun pickRandom(count: Int, excludeId: Int): List<Int> {

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

        fun generateLinks(node: Node, all: List<Node>) {
            val count = randLinkCount()
            val links = pickRandom(count, node.id)

            links.forEach {
                node.mutableLinks.add(Link(all[it], randomWeight()))
            }

        }

        fun genNodes() = (0 until size)
            .map(::createNode)
            .apply {
                onEach {
                    generateLinks(it, this)
                    it.freeze()
                }
            }

        fun genGraph() = Graph(genNodes())

    }

    fun gen(size: Int) = Generator(size).genGraph()
}
