package org.fpeterek.pa.im.cli

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

object CLIOptions {

    operator fun CommandLine.contains(opt: String) = hasOption(opt)

    fun CommandLine.getInt(opt: String) = getOptionValue(opt).toInt()
    fun CommandLine.getString(opt: String) = getOptionValue(opt)!!

    private fun createOption(name: String, fn: Option.Builder.() -> Unit) =
        Option.builder(name).apply(fn).build()!!

    private fun createOption(short: String, long: String, fn: Option.Builder.() -> Unit) =
        Option.builder(short).longOpt(long).apply(fn).build()!!

    private fun genGraphOption() = createOption(generate, generateLong) {
        desc("Generate a random graph")
        hasArg(false)
    }

    private fun graphSizeOption() = createOption(randomGraphSize, randomGraphSizeLong) {
        desc("Number of nodes of the randomly generated graph")
        hasArg()
        type(Int::class.java)
    }

    private fun outfileOption() = createOption(outfile, outfileLong) {
        desc("Where the randomly generated graph should be stored")
        hasArg()
        type(String::class.java)
    }

    private fun getSeedsOption() = createOption(getSeeds, getSeedsLong) {
        desc("Get seeds for specified graph")
        hasArg(false)
    }

    private fun numSeedsOption() = createOption(numSeeds, numSeedsLong) {
        desc("Maximum number of seeds")
        hasArg()
        type(Int::class.java)
    }

    private fun infileOption() = createOption(infile, infileLong) {
        desc("Path to input graph")
        hasArg()
        type(String::class.java)
    }

    private fun threadsOption() = createOption(threads, threadsLong) {
        desc("Number of threads")
        hasArg()
        type(Int::class.java)
    }

    const val generate = "g"
    const val randomGraphSize = "s"
    const val outfile = "o"
    const val getSeeds = "s"
    const val numSeeds = "n"
    const val infile = "i"
    const val threads = "t"

    private const val generateLong = "generate"
    private const val randomGraphSizeLong = "randomGraphSize"
    private const val outfileLong = "outfile"
    private const val getSeedsLong = "getSeeds"
    private const val numSeedsLong = "numSeeds"
    private const val infileLong = "infile"
    private const val threadsLong = "threads"

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

