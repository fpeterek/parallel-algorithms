package org.fpeterek.pa.im.graph.io

import org.fpeterek.pa.im.graph.Graph
import org.fpeterek.pa.im.graph.Link
import org.fpeterek.pa.im.graph.Node
import java.io.BufferedWriter
import java.io.File

object GraphWriter {
    fun write(graph: Graph, outfile: String) = File(outfile).bufferedWriter().use {
        writeGraph(graph, it)
    }

    private fun formatLink(link: Link) = "${link.node.id},${link.weight}"

    private fun formatLinks(links: List<Link>) =
        links.joinToString(separator=";") { link -> formatLink(link) }

    private fun formatNode(node: Node) = "${node.id}:${formatLinks(node.links)}"

    private fun writeGraph(graph: Graph, outfile: BufferedWriter) = graph.nodes
        .forEach {
            outfile.write(formatNode(it))
            outfile.newLine()
        }
}
