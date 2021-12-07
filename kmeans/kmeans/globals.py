from typing import Optional
import multiprocessing as mp


data: Optional[list] = None
in_queue: Optional[mp.Queue] = None
out_queue: Optional[mp.Queue] = None
