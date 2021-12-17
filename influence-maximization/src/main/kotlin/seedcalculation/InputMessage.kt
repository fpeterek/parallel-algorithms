package org.fpeterek.pa.im.seedcalculation

import org.fpeterek.pa.im.graph.Node

sealed class InputMessage {
    object End : InputMessage()
    object GetResult : InputMessage()
    class NodeInfluence(val node: Node) : InputMessage()
}
