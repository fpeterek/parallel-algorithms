package org.fpeterek.pa.im.cli

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

object CLIOptions {

    operator fun CommandLine.contains(opt: String) = hasOption(opt)

    fun CommandLine.getInt(opt: String) = getOptionValue(opt).toInt()
    fun CommandLine.getString(opt: String) = getOptionValue(opt)

    private fun createOption(name: String, fn: Option.Builder.() -> Unit) =
        Option.builder(name).apply(fn).build()!!

    private fun genGraphOption() = createOption(generate) {
        desc("Generate a random graph")
        hasArg(false)
    }

    private fun graphSizeOption() = createOption(randomGraphSize) {
        desc("Number of nodes of the randomly generated graph")
        hasArg()
        type(Int::class.java)
    }

    private fun outfileOption() = createOption(outfile) {
        desc("Where the randomly generated graph should be stored")
        hasArg()
        type(String::class.java)
    }

    private fun getSeedsOption() = createOption(getSeeds) {
        desc("Get seeds for specified graph")
        hasArg(false)
    }

    private fun numSeedsOption() = createOption(numSeeds) {
        desc("Maximum number of seeds")
        hasArg()
        type(Int::class.java)
    }

    private fun infileOption() = createOption(infile) {
        desc("Path to input graph")
        hasArg()
        type(String::class.java)
    }

    private fun threadsOption() = createOption(threads) {
        desc("Number of threads")
        hasArg()
        type(Int::class.java)
    }

    const val generate = "generate"
    const val randomGraphSize = "randomGraphSize"
    const val outfile = "outfile"
    const val getSeeds = "getSeeds"
    const val numSeeds = "numSeeds"
    const val infile = "infile"
    const val threads = "threads"

    private fun createOptions() = Options()
        .addOption(genGraphOption())
        .addOption(graphSizeOption())
        .addOption(outfileOption())
        .addOption(getSeedsOption())
        .addOption(numSeedsOption())
        .addOption(infileOption())
        .addOption(threadsOption())

    fun parse(args: Array<String>) = DefaultParser().parse(createOptions(), args)!!
}

