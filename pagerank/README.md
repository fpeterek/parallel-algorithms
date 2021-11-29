# Pagerank

## Dependencies

* CMake
* A compiler supporting C++17 (including `iso646.h`)
* `pthread`, if you wish to make use of the provided CMake config
    * Alternatively, you can modify the CMake config and provide your own multithreading solution

## Compilation

### Linux

```sh
cmake . -DCMAKE_BUILD_TYPE=Release
make
```

## Generating datasets

A rather inefficient dataset generator is included with the project.

The generator requires Python 3.9 or later to function properly, though it does not have any
dependencies (not even `click`, hence the terrible handling of CLI args).

You can generate a dataset by running the following command:

```sh
./scripts/gen.py in/dataset.graph 1000 50
```

Where the following applies.

```sh
# output file = in/dataset.graph
# number of nodes in the graph = 1000
# maximum number links heading from one node = 50
```

## Running the program

The path to the input graph is expected as the very first CLI argument.
The input graph always has to be specified.


```sh
./pagerank in/small.graph
```

All other arguments are optional. Such arguments include, in this order:

1. `iterations` - a positive integer specifying the number of Pageranks iterations to be performed
2. `threads` - the number of threads to parallelize the Pageranks algorithm on
3. `loggingLevel` - self-explanatory, one of the following values
    * `verbose` - print the entire graph (not recommended for large graphs)
    * `minimal` - only print basic graph info and sum of all pageranks
    * default (i.e. unspecified or invalid value) level logs the all of `minimal` as well as pageranks for all pages (nodes)

I did not bother with proper parsing of CLI arguments, thus, should you wish to specify
a certain argument, you must also specify all preceding arguments.

```sh
./pagerank in/rather_big.graph 10  # 10 iterations
./pagerank in/rather_big.graph 10 5  # 10 iterations, 5 threads
./pagerank in/rather_big.graph 10 5 minimal  # 10 iterations, 5 threads, minimal logging level
```

```sh
# Generate a large dataset (takes some time)
❯ ./scripts/gen.py in/largest.graph 1000000 100  

# Test multithreading
❯ time ./pagerank in/largest.graph 10 1 minimal
Graph {size=1000000, dangling=9951}

Sum of pageranks: 1
./pagerank in/largest.graph 10 1 minimal  93.36s user 0.65s system 99% cpu 1:34.49 total

❯ time ./pagerank in/largest.graph 10 10 minimal
Graph {size=1000000, dangling=9951}

Sum of pageranks: 1
./pagerank in/largest.graph 10 10 minimal  96.32s user 0.78s system 176% cpu 55.001 total

❯ time ./pagerank in/largest.graph 10 20 minimal
Graph {size=1000000, dangling=9951}

Sum of pageranks: 1
./pagerank in/largest.graph 10 20 minimal  98.27s user 0.66s system 188% cpu 52.467 total
```
