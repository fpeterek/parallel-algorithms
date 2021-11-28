//
// Created by fpeterek on 28.11.21.
//

#ifndef PAGERANK_GRAPH_LOADER_HPP
#define PAGERANK_GRAPH_LOADER_HPP

#include "graph.hpp"

#include <string>

class GraphLoader {

    static Node parseLine(const std::string & line);
    static Graph loadFile(const std::string & filename);

public:
    GraphLoader() = delete;
    static Graph loadGraph(const std::string & filename);
};

#endif //PAGERANK_GRAPH_LOADER_HPP
