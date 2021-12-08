import multiprocessing as mp

import globals
from message import Message


def setup(points, processes):
    globals.data = points
    if processes > 1:
        globals.in_queue = mp.Queue()
        globals.out_queue = mp.Queue()


def shutdown(processes):
    globals.data = None

    if processes is not None:
        for _ in processes:
            globals.in_queue.put(Message(msg_type=Message.Type.STOP))
        for p in processes:
            p.join()
            p.close()

    if globals.in_queue is not None:
        globals.in_queue.close()
        globals.in_queue = None
    if globals.out_queue is not None:
        globals.out_queue.close()
        globals.out_queue = None

