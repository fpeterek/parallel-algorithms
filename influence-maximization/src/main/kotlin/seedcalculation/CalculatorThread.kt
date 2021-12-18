package seedcalculation

import org.fpeterek.pa.im.graph.Node
import org.fpeterek.pa.im.infectionmodels.IndependentCascade
import org.fpeterek.pa.im.seedcalculation.InputMessage
import org.fpeterek.pa.im.seedcalculation.ThreadResult
import java.util.concurrent.LinkedBlockingQueue


class CalculatorThread(
    private val inQueue: LinkedBlockingQueue<InputMessage>,
    private val outQueue: LinkedBlockingQueue<ThreadResult>,
    private val infected: Set<Node>) {

    private var bestSeed: Node? = null
    private var highestInfluence: Set<Node> = emptySet()
    private var running = true

    private fun pollQueue() = inQueue.take()!!

    private fun getInfluence(node: Node) {
        val influence = IndependentCascade(node, infected)

        if (influence.size > highestInfluence.size) {
            bestSeed = node
            highestInfluence = influence
        }
    }

    private fun sendResult() {
        outQueue.put(ThreadResult(bestSeed, highestInfluence))
    }

    private fun reset() {
        bestSeed = null
        highestInfluence = emptySet()
    }

    private fun end() {
        sendResult()
        reset()
        running = false
    }

    private fun handleMessage(inputMessage: InputMessage) = when (inputMessage) {
        is InputMessage.End -> end()
        is InputMessage.NodeInfluence -> getInfluence(inputMessage.node)
    }

    fun threadLoop() {
        while (running) {
            handleMessage(pollQueue())
        }
    }
}