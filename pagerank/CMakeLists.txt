cmake_minimum_required(VERSION 3.20)
project(pagerank)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS_RELEASE "-O3")

include_directories(/usr/include/ ./include/)
link_directories(/usr/lib/)

link_libraries(
    pthread
)

add_executable(
    pagerank
    src/main.cpp
    include/node.hpp
    src/node.cpp
    include/graph.hpp src/graph.cpp include/graph_loader.hpp src/graph_loader.cpp include/pagerank_calculator.hpp src/pagerank_calculator.cpp)
