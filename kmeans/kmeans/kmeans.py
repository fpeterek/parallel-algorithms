import multiprocessing as mp
import random

import kmeans_setup as ks
import kmeans_core as kc
import kmeans_distribution as kd
import globals


def run_iteration(points: list[tuple[int, int]], cluster_count: int, processes: int):
    centroids = random.sample(points, cluster_count)
    max_iterations = 10
    dist_fn = [kd.distribute_in_serial, kd.distribute_in_parallel][processes > 1]
    centroid_fn = [kd.calc_centroids_in_serial, kd.calc_centroids_in_parallel][processes > 1]

    for i in range(max_iterations):
        clusters = dist_fn(centroids, points, processes)
        new_centroids = centroid_fn(clusters)
        if new_centroids == centroids:
            break
        centroids = new_centroids

    return centroids, clusters


def run_process():
    while True:
        p_in = globals.in_queue.get()
        if p_in is False:
            return
        centroids, split = p_in
        kd.distribute_points(centroids, split)


def create_processes(num_processes):
    if num_processes < 2:
        return []
    processes = [mp.Process(target=run_process) for _ in range(num_processes)]
    for p in processes:
        p.start()
    return processes


def kmeans(points: list[tuple[int, int]], cluster_count: int, attempts: int, processes: int) -> list[list[tuple[int, int]]]:
    best_sse = None
    best_distribution = None

    ks.setup(points, processes)
    pool = create_processes(processes)

    for i in range(attempts):
        centroids, clusters = run_iteration(points, cluster_count, processes)
        sse = kc.calc_dist_sse(centroids, clusters)
        if best_sse is None or sse < best_sse:
            best_sse = sse
            best_distribution = clusters

    ks.shutdown(pool)

    return best_distribution

