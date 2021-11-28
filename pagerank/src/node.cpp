//
// Created by fpeterek on 28.11.21.
//

#include "node.hpp"

void Node::applyRank() {
    _pagerank = _nextrank;
    _nextrank = 0;
}

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

void Node::addToRank(double delta) {
    _nextrank += delta;
}

double Node::linkValue() const {
    return pagerank() / linkCount();
}

std::size_t Node::linkCount() const {
    return _links.size();
}

Node::Node(Node && other) noexcept :
    _id(other._id), _links(std::move(other._links)),
    _pagerank(other._pagerank), _nextrank(other._nextrank) { }

Node & Node::operator=(Node && other) noexcept {
    _id = other._id;
    _links = std::move(other._links);
    _pagerank = other._pagerank;
    _nextrank = other._nextrank;
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
