//
// Created by fpeterek on 28.11.21.
//

#include "pagerank_calculator.hpp"

#include <cmath>
#include <iostream>


void PagerankCalculator::pagerank(Graph & graph, const std::uint64_t iterations, const std::size_t threads)  {
    PagerankCalculator(graph, iterations, threads).calcPageranks();
}

PagerankCalculator::PagerankCalculator(Graph & graph, const std::uint64_t iterations, const std::size_t threadCount) :
        graph(graph), iterations(iterations), threadCount(threadCount) {

    threads.reserve(threadCount);
    splits.reserve(threadCount);

    const std::size_t splitSize = std::ceil(graph.nodeCount() / (double)threadCount);

    for (std::size_t begin = 0; begin < graph.nodeCount(); begin += splitSize) {
        std::size_t end = begin + splitSize-1;
        if (end >= graph.nodeCount()) {
            end = graph.nodeCount()-1;
        }
        splits.emplace_back(begin, end);
    }

    nextPageranks.reserve(graph.nodeCount());
    for (std::size_t i = 0; i < graph.nodeCount(); ++i) {
        nextPageranks.emplace_back(0.0);
    }
}

void PagerankCalculator::pagerankOnRange(std::pair<std::size_t, std::size_t> range) {
    const auto [begin, end] = range;

    for (std::size_t i = begin; i <= end; ++i) {
        Node & node = graph[i];

        auto inverted = invertedIndex.find(node.id());

        // If no nodes link to current node, continue to next node
        if (inverted == invertedIndex.end()) {
            continue;
        }

        // Increase pagerank by link weight of each node linking to the current node
        for (const auto linkingId : inverted->second) {
            nextPageranks[i] += graph[linkingId].linkValue();
        }
    }
}

void PagerankCalculator::danglingAndApplyOnRange(std::pair<std::size_t, std::size_t> range) {
    const auto [begin, end] = range;

    for (std::size_t i = begin; i <= end; ++i) {
        Node & node = graph[i];
        node.setPagerank(nextPageranks[i] + danglingWeight);
        nextPageranks[i] = 0;
    }
}

void PagerankCalculator::pagerankIteration() {
    parallelize([this](std::pair<std::size_t, std::size_t> range) { pagerankOnRange(range); });
}

void PagerankCalculator::applyDanglingAndApplyPR() {

    // Redistribute the sum of dangling (i.e. linking to no pages) nodes
    // Over every single node across the graph
    danglingWeight = 0;
    for (const auto dangling : graph.dangling()) {
        danglingWeight += dangling.get().pagerank();
    }
    danglingWeight /= graph.nodeCount();

    parallelize([this](std::pair<std::size_t, std::size_t> range) { danglingAndApplyOnRange(range); });
}

void PagerankCalculator::calcPageranks() {

    buildInvertedIndex();
    initializePageranks();

    for (std::uint64_t i = 0; i < iterations; ++i) {
        pagerankIteration();
        applyDanglingAndApplyPR();
        // applyRanks();
    }

}

void PagerankCalculator::initializeOnRange(std::pair<std::size_t, std::size_t> range) {
    const auto [begin, end] = range;
    const double init = 1.0 / graph.nodeCount();

    for (std::size_t i = begin; i <= end; ++i) {
        graph[i].setPagerank(init);
    }
}

void PagerankCalculator::initializePageranks() {
    parallelize([this](std::pair<std::size_t, std::size_t> range) { initializeOnRange(range); });
}

void PagerankCalculator::buildInvertedIndex() {
    for (const auto & node : graph.nodes()) {
        for (const auto link : node.links()) {
            invertedIndex[link].emplace_back(node.id());
        }
    }
    for (auto & [k, v] : invertedIndex) {
        v.shrink_to_fit();
    }
}

void PagerankCalculator::parallelize(const std::function<void(std::pair<std::size_t, std::size_t>)> & fn) {
    for (const auto range : splits) {
        threads.emplace_back(fn, range);
    }
    for (auto & thread : threads) {
        thread.join();
    }
    threads.clear();
}
