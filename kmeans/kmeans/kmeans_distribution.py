import math

import globals
from kmeans_core import calc_centroid, distribute_points
from message import Message


def calc_centroids_in_serial(clusters) -> list[tuple[float, float]]:
    return [calc_centroid(c) for c in clusters]


def calc_centroids_in_parallel(clusters):
    for idx, cluster in enumerate(clusters):
        globals.in_queue.put(Message(msg_type=Message.Type.CENTROIDS, data=cluster, order=idx))
    centroids = [globals.out_queue.get() for _ in range(len(clusters))]
    centroids.sort(key=lambda msg: msg.order)
    centroids = [msg.data for msg in centroids]


def calc_ranges(points, processes) -> list[range]:
    splits = []
    split_size = math.ceil(len(points) / processes)
    for i in range(processes):
        begin = i * split_size
        end = min((i+1) * split_size, len(points))
        splits.append(range(begin, end))
        if end >= len(points):
            break

    return splits


def distribute_in_serial(centroids, points, _):
    res = distribute_points(centroids, range(len(points)))
    return res


def merge_results(messages: list[Message]) -> list[list]:
    res = []
    for msg in messages:
        for idx, cluster in enumerate(msg.data):
            if len(res) <= idx:
                res.append([])
            res[idx] += cluster.data
    return res


def distribute_in_parallel(centroids, points, processes):
    splits = calc_ranges(points, processes)

    for split in splits:
        globals.in_queue.put(Message(msg_type=Message.Type.CLUSTERIZE, data=(centroids, split)))
    res = [globals.out_queue.get() for _ in range(len(splits))]

    return merge_results(res)
