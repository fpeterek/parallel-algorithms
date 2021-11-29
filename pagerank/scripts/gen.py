#!/usr/bin/python3

import sys
import random


class Node:
    def __init__(self, node_id: int, links: list[int] = None):
        self.id = node_id
        self.links = links if links is not None else []


def gen(page_count: int, max_links: int) -> list[Node]:
    nodes = [Node(i) for i in range(page_count)]

    link_list = list(range(page_count))

    for node in nodes:
        link_count = random.randint(0, max_links)

        link_list.remove(node.id)
        node.links = random.sample(link_list, link_count)
        link_list.append(node.id)

    return nodes


def save_nodes(nodes: list[Node], out_file: str) -> None:
    with open(out_file, 'w') as out:
        for node in nodes:
            line = f'{node.id}:{",".join(map(lambda num: str(num), node.links))}'
            out.write(line)
            out.write('\n')


def main() -> None:
    out_file = sys.argv[1] if len(sys.argv) >= 2 else 'pages.graph'
    page_count = int(sys.argv[2]) if len(sys.argv) >= 3 else 100
    max_links = int(sys.argv[3]) if len(sys.argv) >= 4 else 10
    nodes = gen(page_count, max_links)
    save_nodes(nodes, out_file)


if __name__ == '__main__':
    main()

