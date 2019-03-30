import random
import copy
import networkx.algorithms.shortest_paths as alg
import networkx as nx
import matplotlib.pyplot as plt


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


def count_probability(graph, amount):
    successes = 0.0
    for i in range(0, amount):
        clone = copy.deepcopy(graph)
        random_remove(clone)
        if check_connectivity(clone):
            successes = successes + 1
    return successes/amount


def check_connectivity(graph):
    for v in graph.nodes:
        if not alg.has_path(graph, 1, v):
            return False
    return True


def draw_graph(graph):
    pos = nx.spring_layout(graph)
    nx.draw_networkx_nodes(graph, pos)

    w_edg = nx.get_edge_attributes(graph, 'weight')

    nx.draw_networkx_edges(graph, pos, alpha=0.5)
    nx.draw_networkx_edge_labels(graph, pos, edge_labels=w_edg, font_size=7)

    nx.draw_networkx_labels(graph, pos)

    plt.axis('off')
    plt.show()
