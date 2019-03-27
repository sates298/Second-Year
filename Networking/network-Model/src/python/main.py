import networkx as nx
import networkx.algorithms.shortest_paths as alg
import matplotlib.pyplot as plt
import random


def draw_graph(graph):
    nx.draw(graph, with_labels=True, font_weight='bold')
    plt.show()


def check_connectivity(graph):
    for v in graph.nodes:
        if not alg.has_path(graph, 1, v):
            return False
    return True


def random_remove(graph):
    to_drop = []
    visited = []
    for n, nbrs in graph.adj.items():
        visited.append(n)
        for nbr, eattr in nbrs.items():
            wt = eattr['weight']
            if not visited.__contains__(nbr):
                r = random.uniform(0.0, 1.0)
                if wt < r:
                    to_drop.append((n, nbr))
    graph.remove_edges_from(to_drop)


if __name__ == "__main__":
    model = nx.Graph()
    model.add_nodes_from([i for i in range(1, 21)])
    model.add_weighted_edges_from([(i, i + 1, 0.95) for i in range(1, 20)])

    random_remove(model)
    draw_graph(model)
