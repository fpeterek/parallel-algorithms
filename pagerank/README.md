# Pagerank

## Dependencies

* CMake
* A reasonable compiler (MSVC may not suffice due to my abuntant usage of `iso646.h`) supporting C++17
* `pthread`, if you wish to use the provided CMake config
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
❯ time ./pagerank in/medium.graph 10 5 minimal
Sum of pageranks: 1
./pagerank in/medium.graph 10 5  0.01s user 0.01s system 127% cpu 0.013 total
❯ time ./pagerank in/above_medium.graph 10 5 minimal
Sum of pageranks: 1
./pagerank in/above_medium.graph 10 5 minimal  0.21s user 0.01s system 140% cpu 0.151 total
❯ time ./pagerank in/above_medium.graph 10 1 minimal
Sum of pageranks: 1
./pagerank in/above_medium.graph 10 1 minimal  0.21s user 0.01s system 99% cpu 0.214 total
❯ time ./pagerank in/rather_big.graph 10 1 minimal
Sum of pageranks: 1
./pagerank in/rather_big.graph 10 1 minimal  20.52s user 0.19s system 99% cpu 20.729 total
❯ time ./pagerank in/rather_big.graph 10 8 minimal
Sum of pageranks: 1
./pagerank in/rather_big.graph 10 8 minimal  20.16s user 0.21s system 153% cpu 13.241 total
❯ time ./pagerank in/rather_big.graph 10 16 minimal
Sum of pageranks: 1
./pagerank in/rather_big.graph 10 16 minimal  20.23s user 0.19s system 155% cpu 13.170 total
❯ time ./pagerank in/rather_big.graph 10 20 minimal
Sum of pageranks: 1
./pagerank in/rather_big.graph 10 20 minimal  20.47s user 0.19s system 154% cpu 13.331 total
```
