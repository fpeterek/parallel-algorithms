package org.fpeterek.pa.im.graph

class Link(val node: Node, val weight: Double) {
    init {
        if (weight < 0.0 || weight > 1.0) {
            throw IllegalArgumentException("Link weight must fall in range <0.0; 1.0>.")
        }
    }
}
