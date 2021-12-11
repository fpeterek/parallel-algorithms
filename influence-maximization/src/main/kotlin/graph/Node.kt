package org.fpeterek.pa.im.graph


class Node(val id: Int, private val _links: MutableList<Link>) {

    var frozen = false
        set(value) {
            if (field) {
                throw RuntimeException("Mutation of frozen node is forbidden")
            }
            field = value
        }

    val links: List<Link>
        get() = _links

    val mutableLinks: MutableList<Link>
        get() = if (frozen) {
            throw RuntimeException("Access to mutable collection of a frozen node is forbidden")
        } else {
            _links
        }

    fun freeze() {
        frozen = true
    }

}
