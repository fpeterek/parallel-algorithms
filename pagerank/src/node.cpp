//
// Created by fpeterek on 28.11.21.
//

#include "node.hpp"

Node::Node(const std::uint64_t id, std::vector<std::uint64_t> links) : _id(id), _links(std::move(links)) { }

std::uint64_t Node::id() const {
    return _id;
}

const std::vector<std::uint64_t> & Node::links() const {
    return _links;
}

double Node::pagerank() const {
    return _pagerank;
}

void Node::setPagerank(const double newRank) {
    _pagerank = newRank;
}

double Node::linkValue() const {
    return pagerank() / linkCount();
}

std::size_t Node::linkCount() const {
    return _links.size();
}

Node::Node(Node && other) noexcept :
    _id(other._id), _links(std::move(other._links)), _pagerank(other._pagerank) { }

Node & Node::operator=(Node && other) noexcept {
    _id = other._id;
    _links = std::move(other._links);
    _pagerank = other._pagerank;
    return *this;
}

bool Node::isDangling() const {
    return links().empty();
}

void printWithLinks(std::ostream & os, const Node & node) {
    os << "Node {id=" << node.id() << ", links=[";

    for (std::size_t i = 0; i < node.linkCount(); ++i) {
        auto link = node.links()[i];
        os << link;
        if (i < node.linkCount()-1) {
            os << ", ";
        }
    }

    os << "]}";
}

std::ostream & operator<<(std::ostream & os, const Node & node) {
    return os << "Node {id=" << node.id() << ", pagerank=" << node.pagerank() << "}";
}
