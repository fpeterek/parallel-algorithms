# Influence Maximization

Greedy approach to the Influence Maximization problem
using the Independent Cascade model.

## Dependencies

* JVM 17

## Compilation

We can use the Gradle wrapper to compile the code and
package the jar. Gradle is configured to create a fatjar,
meaning all the dependencies will be bundled inside the jar.
Thus, we do not need to install the libraries locally.

```sh
./gradlew clean build
```

## Running the program

### Generating a random dataset

**This step is not necessary as the medium sized dataset is already
included in the repository**

We can generate a random dataset by passing the following arguments.

```sh
java -jar build/libs/influence-maximization-1.0.jar -generate -randomGraphSize 10000 -maxLinks 50 -outfile graphs/medium.graph
```

It is, of course, possible to modify the values of said arguments, however,
I've found the previous configuration works the best as it takes only a few
minutes to generate the dataset and about 90 seconds to process it using
a single thread on my machine, which makes it a reasonable candidate for
benchmarking the performance of the code and measuring the performance
implications of increasing the number of threads.

### Running the Influence Maximization algorithm

We can run the algorithm and get a list of reasonable seeds
by passing the following arguments.

```sh
# Single thread - no parallelism
java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile graphs/medium.graph -numSeeds 5 -threads 1

# Run in parallel on four threads 
java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile graphs/medium.graph -numSeeds 5 -threads 4
```

## Benchmarking the code

Once again, we can use the `time` utility to measure the running time
of the program.

```sh
❯ time java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile graphs/medium.graph -numSeeds 5 -threads 1
Seeds: 7418, 5159, 6212, 7198, 6436
java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile   5  1  88.76s user 0.35s system 100% cpu 1:28.68 total

❯ time java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile graphs/medium.graph -numSeeds 5 -threads 4
Seeds: 6100, 3480, 9295, 3593, 1939
java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile   5  4  104.29s user 0.40s system 387% cpu 27.043 total

❯ time java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile graphs/medium.graph -numSeeds 5 -threads 8
Seeds: 6640, 1833, 7362, 3073, 2582
java -jar build/libs/influence-maximization-1.0.jar -getSeeds -infile   5  8  109.19s user 0.55s system 382% cpu 28.658 total
```
