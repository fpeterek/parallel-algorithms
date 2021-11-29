//
// Created by fpeterek on 28.11.21.
//

#ifndef PAGERANK_NODE_HPP
#define PAGERANK_NODE_HPP

#include <cstdint>
#include <vector>
#include <ostream>

class Node {

    std::uint64_t _id;
    std::vector<std::uint64_t> _links;

    double _pagerank = 0.0;

public:
    Node(std::uint64_t id, std::vector<std::uint64_t> links);
    Node(const Node & other) = default;
    Node(Node && other) noexcept;

    Node & operator=(const Node & other) = default;
    Node & operator=(Node && other) noexcept;

    std::uint64_t id() const;
    const std::vector<std::uint64_t> & links() const;
    double pagerank() const;
    double linkValue() const;
    std::size_t linkCount() const;
    bool isDangling() const;

    void setPagerank(double pagerank);

};

void printWithLinks(std::ostream & os, const Node & node);
std::ostream & operator<<(std::ostream & os, const Node & node);

#endif //PAGERANK_NODE_HPP
