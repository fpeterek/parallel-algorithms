//
// Created by fpeterek on 28.11.21.
//

#ifndef PAGERANK_PAGERANK_CALCULATOR_HPP
#define PAGERANK_PAGERANK_CALCULATOR_HPP

#include "graph.hpp"

#include <functional>
#include <unordered_map>
#include <thread>

class PagerankCalculator {

    explicit PagerankCalculator(Graph & graph, std::uint64_t iterations, std::size_t threads);

    const size_t threadCount;
    const std::uint64_t iterations;

    Graph & graph;
    std::unordered_map<std::uint64_t, std::vector<std::uint64_t>> invertedIndex;
    std::vector<double> nextPageranks;
    double danglingWeight = 0.0;

    std::vector<std::thread> threads;

    void buildInvertedIndex();
    void initializePageranks();
    void calcPageranks();

    std::vector<std::pair<std::size_t, std::size_t>> splits;

    void parallelize(const std::function<void(std::pair<std::size_t, std::size_t>)> & fn);

    void initializeOnRange(std::pair<std::size_t, std::size_t> range);

    void pagerankOnRange(std::pair<std::size_t, std::size_t> range);
    void danglingAndApplyOnRange(std::pair<std::size_t, std::size_t> range);
    void applyRanksOnRange(std::pair<std::size_t, std::size_t> range);

    void pagerankIteration();
    void applyDanglingAndApplyPR();
    void applyRanks();

public:

    static void pagerank(Graph & graph, std::uint64_t iterations, std::size_t threads);
};

#endif //PAGERANK_PAGERANK_CALCULATOR_HPP
