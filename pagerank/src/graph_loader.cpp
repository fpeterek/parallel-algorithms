//
// Created by fpeterek on 28.11.21.
//

#include "graph_loader.hpp"

#include <fstream>
#include <sstream>
#include <limits>
#include <iostream>


Graph GraphLoader::loadGraph(const std::string & filename) {
    return loadFile(filename);
}

Node GraphLoader::parseLine(const std::string & line) {

    std::stringstream ss(line);

    std::uint64_t id;
    std::vector<std::uint64_t> links;
    ss >> id;
    ss.ignore(std::numeric_limits<std::streamsize>::max(), ':');

    while (ss and ss.peek() != '\n' and not ss.eof()) {
        std::uint64_t link;
        ss >> link;
        links.emplace_back(link);
        ss.ignore(std::numeric_limits<std::streamsize>::max(), ',');
    }

    links.shrink_to_fit();

    return { id, std::move(links) };
}

Graph GraphLoader::loadFile(const std::string & filename) {

    std::ifstream is(filename);
    std::string line;
    std::vector<Node> nodes;

    while (is) {
        std::getline(is, line);
        if (not line.empty()) {
            nodes.emplace_back(parseLine(line));
        }
    }

    nodes.shrink_to_fit();

    return { nodes };
}
