package org.fpeterek.pa.im

import org.apache.commons.cli.CommandLine
import org.fpeterek.pa.im.cli.CLIOptions
import org.fpeterek.pa.im.cli.CLIOptions.contains
import org.fpeterek.pa.im.cli.CLIOptions.getInt
import org.fpeterek.pa.im.cli.CLIOptions.getString
import org.fpeterek.pa.im.graph.gen.GraphGenerator
import org.fpeterek.pa.im.graph.io.GraphIO.loadGraphFromPath
import org.fpeterek.pa.im.graph.io.GraphIO.save
import org.fpeterek.pa.im.seedcalculation.SeedCalculator

fun generate(size: Int, outfile: String, maxLinks: Int) =
    GraphGenerator.gen(size, maxLinks).save(outfile)

fun generate(cl: CommandLine) {

    val size = cl.getInt(CLIOptions.randomGraphSize)
    val outfile = cl.getString(CLIOptions.outfile)
    val maxLinks = cl.getInt(CLIOptions.maxLinks)

    if (size < 0) {
        println("Invalid graph size")
    } else if (outfile.isBlank()) {
        println("Invalid graph size")
    } else {
        generate(size, outfile, maxLinks)
    }
}

fun getSeeds(infile: String, numSeeds: Int, threads: Int) {

    val graph = infile.loadGraphFromPath()
    println("Seeds:")
    SeedCalculator.getSeeds(graph, numSeeds, threads).forEach { println(it.id) }

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
            println("Exclusive options '${CLIOptions.getSeedsLong}' and '${CLIOptions.generateLong}'")
        }
        CLIOptions.generate in options -> generate(options)
        CLIOptions.getSeeds in options -> getSeeds(options)
        else -> println("No action specified")
    }
}
