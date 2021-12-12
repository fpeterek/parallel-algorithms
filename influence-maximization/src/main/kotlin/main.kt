package org.fpeterek.pa.im

import org.apache.commons.cli.CommandLine
import org.fpeterek.pa.im.cli.CLIOptions
import org.fpeterek.pa.im.cli.CLIOptions.contains
import org.fpeterek.pa.im.cli.CLIOptions.getInt
import org.fpeterek.pa.im.cli.CLIOptions.getString
import org.fpeterek.pa.im.graph.gen.GraphGenerator
import org.fpeterek.pa.im.graph.io.GraphIO.loadGraphFromPath
import org.fpeterek.pa.im.graph.io.GraphIO.save

fun generate(size: Int, outfile: String) = GraphGenerator.gen(size).let {
    val avgLinks = it.nodes.sumOf { node -> node.links.size } / it.nodes.size
    println(avgLinks)
    it.save(outfile)
}

fun generate(cl: CommandLine) {

    val size = cl.getInt(CLIOptions.randomGraphSize)
    val outfile = cl.getString(CLIOptions.outfile)

    if (size < 0) {
        println("Invalid graph size")
    } else if (outfile.isBlank()) {
        println("Invalid graph size")
    } else {
        generate(size, outfile)
    }
}

fun getSeeds(infile: String, numSeeds: Int, threads: Int) {

    val graph = infile.loadGraphFromPath()
    val avgLinks = graph.nodes.sumOf { it.links.size } / graph.nodes.size.toDouble()
    println(avgLinks)

}

fun getSeeds(cl: CommandLine) {

    val infile = cl.getString(CLIOptions.infile)
    val numSeeds = cl.getInt(CLIOptions.numSeeds)
    val threads = cl.getInt(CLIOptions.threads)

    if (numSeeds < 1) {
        println("Number of seeds should be at least 1")
    } else if (infile.isBlank()) {
        println("Input graph not specified")
    } else if (threads < 1) {
        println("Number of threads must be at least 1")
    } else {
        getSeeds(infile, numSeeds, threads)
    }
}

fun main(args: Array<String>) = CLIOptions.parse(args).let { options ->
    when {
        CLIOptions.generate in options && CLIOptions.getSeeds in options -> {
            println("Exclusive options '${CLIOptions.getSeeds}' and '${CLIOptions.generate}'")
        }
        CLIOptions.generate in options -> generate(options)
        CLIOptions.getSeeds in options -> getSeeds(options)
        else -> println("No action specified")
    }
}
