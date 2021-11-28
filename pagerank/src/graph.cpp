//
// Created by fpeterek on 28.11.21.
//

#include "graph.hpp"

std::vector<Node> & Graph::nodes() {
    return _nodes;
}

Graph::Graph(Graph && other) noexcept : _nodes(std::move(other._nodes)) {
    if (other._dangling.has_value()) {
        _dangling.emplace(std::move(_dangling.value()));
    }
}

void Graph::calcDangling() {
    _dangling.emplace();
    auto & vector = _dangling.value();

    for (auto & node : _nodes) {
        if (node.isDangling()) {
            vector.emplace_back(node);
        }
    }
}

const std::vector<std::reference_wrapper<Node>> & Graph::dangling() const {
    if (_dangling.has_value()) {
        return _dangling.value();
    }
    throw std::runtime_error("List of dangling nodes unavailable.");
}

std::vector<std::reference_wrapper<Node>> & Graph::dangling() {
    if (not _dangling.has_value()) {
        calcDangling();
    }
    return _dangling.value();
}

std::size_t Graph::nodeCount() const {
    return _nodes.size();
}

Graph::Graph(std::vector<Node> nodes) : _nodes(std::move(nodes)) { }

std::size_t Graph::danglingCount() const {
    if (not _dangling.has_value()) {
        throw std::runtime_error("List of dangling nodes unavailable.");
    }
    return _dangling.value().size();
}

const std::vector<Node> & Graph::nodes() const {
    return _nodes;
}

std::size_t Graph::danglingCount() {
    return dangling().size();
}

Node & Graph::operator[](const std::size_t index) {
    return nodes()[index];
}

const Node & Graph::operator[](const std::size_t index) const {
    return nodes()[index];
}

bool Graph::danglingAvailable() const {
    return _dangling.has_value();
}

bool Graph::danglingAvailable() {
    return true;
}

std::ostream & operator<<(std::ostream & os, const Graph & g) {
    os << "Graph {size=" << g.nodeCount() << ", dangling=";
    if (g.danglingAvailable()) {
        os << g.danglingCount();
    } else {
        os << "unknown";
    }
    return os << "}" << std::endl;
}

void printGraphNodes(std::ostream & os, const Graph & g) {
    for (auto & node : g.nodes()) {
        printWithLinks(os, node);
        std::endl(os);
    }
}

std::ostream & operator<<(std::ostream & os, Graph & g) {
    return os << "Graph {size=" << g.nodeCount() << ", dangling=" << g.danglingCount() << "}" << std::endl;
}
