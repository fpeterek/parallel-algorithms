package org.fpeterek.pa.im.graph.io

import org.fpeterek.pa.im.graph.Graph

object GraphIO {
    fun load(filename: String) = GraphLoader.load(filename)
    fun write(graph: Graph, outfile: String) = GraphWriter.write(graph, outfile)

    fun String.loadGraphFromPath() = load(this)
    fun Graph.save(outfile: String) = write(this, outfile)
}
