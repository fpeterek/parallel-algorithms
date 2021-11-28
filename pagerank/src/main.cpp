#include <iostream>
#include <string>
#include <cstring>

#include "graph_loader.hpp"
#include "pagerank_calculator.hpp"
#include "graph.hpp"



int main(int argc, const char * argv[]) {

    if (argc == 1) {
        std::cout << "No file passed in." << std::endl;
        return 1;
    }

    std::uint64_t iterations = 5;
    std::uint64_t threads = 5;
    const bool verbose = argc >= 5 and not std::strcmp(argv[4], "verbose");
    const bool minimal = argc >= 5 and not std::strcmp(argv[4], "minimal");

    if (argc >= 3) {
        iterations = std::stoull(argv[2]);
    }
    if (argc >= 4) {
        threads = std::stoull(argv[3]);
    }

    Graph graph = GraphLoader::loadGraph(argv[1]);

    std::cout << graph << std::endl;
    if (verbose) {
        printGraphNodes(std::cout, graph);
    }

    PagerankCalculator::pagerank(graph, iterations, threads);

    double rankSum = 0;

    for (auto & node : graph.nodes()) {
        if (not minimal) {
            std::cout << node << std::endl;
        }
        rankSum += node.pagerank();
    }

    std::cout << "Sum of pageranks: " << rankSum << std::endl;
}
