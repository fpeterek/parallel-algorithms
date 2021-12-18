# K-means

Parallelized implementation of the K-means clustering algorithm.
Dataset is randomly generated for demonstration purposes.

The preferred environment to run the script is GNU/Linux as the code
somewhat depends on the behaviour of `fork` and it's COW characteristic.
The behaviour of the script is untested on other systems, it should work,
but it is likely to be slower without the ability to utilize `fork`.

## Setup

The preferred way to setup the project is to use `virtualenv` to setup
a virtual environment and install all dependencies inside of it.

```sh
virtualenv venv --python=python3.9
source venv/bin/activate
pip install -r requirements.txt
```

## Running the program

The simplemest way to run the program is to invoke the Python interpreter
and run the code. The module includes a `__main__.py` file, thus, we can
simply run the entire module using the following command.

```sh
python3 kmeans
```

This will run the program using the default configuration.

### Providing custom configuration values

The program can be configured using the following flags.

```
Options:
  --max-x INTEGER      Max x coordinate
  --max-y INTEGER      Max y coordinate
  --points INTEGER     Number of randomly generated points
  --clusters INTEGER   Number of clusters to find
  --attempts INTEGER   Number of clustering attempts
  --processes INTEGER  Number of processes used when parallelizing the
                       algorithm
  --plot BOOLEAN       Whether the clusters should be plotted
  --help               Show this message and exit.
```

Example:

```sh
python3 kmeans --points 1000 --clusters 10 --attempts 10 --processes 4 --plot y
```

## Benchmarking the program

The simplest way to measure the running time of the program is to use the `time`
utility provided by the Linux operating system, or, in some cases, your shell of choice. 
When benchmarking, make sure to set the `--plot` flag to false (for example,
the value of `n` evaluates to false), or just leave it unset.

```sh
time python3 kmeans --points 100000 --clusters 7 --attempts 10 --processes 4
```

### Results

```sh
❯ time python3 kmeans --points 100000 --clusters 7 --attempts 10 --processes 1
python3 kmeans --points 100000 --clusters 7 --attempts 10  1  37.33s user 0.15s system 100% cpu 37.274 total

❯ time python3 kmeans --points 100000 --clusters 7 --attempts 10 --processes 2
python3 kmeans --points 100000 --clusters 7 --attempts 10  2  42.95s user 0.73s system 189% cpu 23.085 total

❯ time python3 kmeans --points 100000 --clusters 7 --attempts 10 --processes 4
python3 kmeans --points 100000 --clusters 7 --attempts 10  4  44.07s user 0.74s system 310% cpu 14.440 total

❯ time python3 kmeans --points 100000 --clusters 7 --attempts 10 --processes 8
python3 kmeans --points 100000 --clusters 7 --attempts 10  8  42.26s user 0.64s system 312% cpu 13.741 total
```

