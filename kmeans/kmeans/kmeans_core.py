import globals


def dist_square(p1, p2) -> float:
    dx = p1[0] - p2[0]
    dy = p1[1] - p2[1]
    return dx**2 + dy**2


def calc_dist(p1, p2) -> float:
    return dist_square(p1, p2) ** 0.5


def calc_centroid(cluster: list[tuple[int, int]]) -> tuple[float, float]:
    sum_x = 0
    sum_y = 0
    for point in cluster:
        sum_x += point[0]
        sum_y += point[1]
    return sum_x / len(cluster), sum_y / len(cluster)


def distribute_points(centroids, indices):
    clusters = [[] for _ in range(len(centroids))]
    for index in indices:
        point = globals.data[index]
        min_centroid = 0
        min_dist = None
        for idx, centroid in enumerate(centroids):
            dist = calc_dist(point, centroid)
            if min_dist is None or dist < min_dist:
                min_dist = dist
                min_centroid = idx
        clusters[min_centroid].append(point)

    if globals.out_queue is not None:
        globals.out_queue.put(clusters)
    return clusters


def calc_dist_sse(centroids, clusters):
    distances = 0
    for centroid, cluster in zip(centroids, clusters):
        for point in cluster:
            distances += dist_square(centroid, point)
    return distances
