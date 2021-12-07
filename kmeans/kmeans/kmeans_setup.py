import multiprocessing as mp

import globals


def setup(points, processes):
    globals.data = points
    if processes > 1:
        globals.in_queue = mp.Queue()
        globals.out_queue = mp.Queue()


def shutdown(processes):
    globals.data = None

    for _ in processes:
        globals.in_queue.put(False)
    for p in processes:
        p.join()
        p.close()

    if globals.in_queue:
        globals.in_queue.close()
        globals.in_queue = None
    if globals.out_queue:
        globals.out_queue.close()
        globals.out_queue = None

