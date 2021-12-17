package org.fpeterek.pa.im.graph.io

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Link
import org.fpeterek.pa.im.graph.Node
import java.io.BufferedReader
import java.io.File
import java.util.stream.Collectors

object GraphLoader {
    fun load(filename: String) = File(filename).bufferedReader().use {
        loadGraph(it)
    }

    private data class LinkPair(val otherId: Int, val weight: Double)
    private data class NodePair(val node: Node, val links: List<LinkPair>)

    private fun loadNodePairs(stream: BufferedReader) = stream.lines()
        .filter { !it.isNullOrBlank() }
        .map { parseNodeLinkPairs(it) }
        .collect(Collectors.toList())

    private fun loadNodes(stream: BufferedReader) = loadNodePairs(stream).let { nodePairs ->
        nodePairs.map { (node, links) ->
            links.forEach { link ->
                val linkedNode = nodePairs[link.otherId].node
                node.mutableLinks.add(Link(linkedNode, link.weight))
            }
            node.freeze()
            node
        }
    }

    private fun loadGraph(stream: BufferedReader) = Graph(loadNodes(stream))

    private fun parseNodeLinkPairs(line: String): NodePair {
        val s1 = line.split(":", limit=2)
        val nodeId = s1.first().toInt()
        val links = s1.last()
            .split(";")
            .filter { it.isNotBlank() }
            .map {
                val split = it.split(",", limit=2)
                LinkPair(split.first().toInt(), split.last().toDouble())
            }

        return NodePair(Node(nodeId), links)
    }
}
