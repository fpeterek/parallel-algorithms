package org.fpeterek.pa.im

import kotlin.concurrent.thread

fun main() {
    listOf("Hello", ", ", "World", "!").map {
        thread { println(it) }
    }
}
