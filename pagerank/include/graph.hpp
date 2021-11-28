//
// Created by fpeterek on 28.11.21.
//

#ifndef PAGERANK_GRAPH_HPP
#define PAGERANK_GRAPH_HPP

#include "node.hpp"

#include <functional>
#include <optional>
#include <algorithm>
#include <exception>
#include <stdexcept>
#include <ostream>


class Graph {

    std::vector<Node> _nodes;
    std::optional<std::vector<std::reference_wrapper<Node>>> _dangling;

    void calcDangling();

public:
    Graph() = delete;
    Graph(const Graph & graph) = default;
    Graph(Graph && other) noexcept;

    Graph(std::vector<Node> nodes);

    std::size_t nodeCount() const;
    std::size_t danglingCount() const;
    const std::vector<Node> & nodes() const;
    const std::vector<std::reference_wrapper<Node>> & dangling() const;
    bool danglingAvailable() const;
    bool danglingAvailable();

    Node & operator[](std::size_t index);
    const Node & operator[](std::size_t index) const;

    std::vector<Node> & nodes();
    std::vector<std::reference_wrapper<Node>> & dangling();
    std::size_t danglingCount();

};

std::ostream & operator<<(std::ostream & os, const Graph & g);
void printGraphNodes(std::ostream & os, const Graph & g);

std::ostream & operator<<(std::ostream & os, Graph & g);

#endif //PAGERANK_GRAPH_HPP
