import networkx as nx
import networkx.algorithms.shortest_paths as alg
import matplotlib.pyplot as plt
import random
import copy


def draw_graph(graph):
    pos = nx.spring_layout(graph)
    nx.draw_networkx_nodes(graph, pos)

    w_edg = nx.get_edge_attributes(graph, 'weight')

    nx.draw_networkx_edges(graph, pos, alpha=0.5)
    nx.draw_networkx_edge_labels(graph, pos, edge_labels=w_edg, font_size=7)

    nx.draw_networkx_labels(graph, pos)

    plt.axis('off')
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


def count_probability(graph):
    amount = 1000
    successes = 0.0
    for i in range(0, amount):
        clone = copy.deepcopy(graph)
        random_remove(clone)
        if check_connectivity(clone):
            successes = successes + 1
    return successes/amount


if __name__ == "__main__":
    model = nx.Graph()
    model.add_nodes_from([i for i in range(1, 21)])
    model.add_weighted_edges_from([(i, i + 1, 0.95) for i in range(1, 20)])

    print("first point: ", count_probability(model))
    draw_graph(model)

    model.add_edge(1, 20)
    model[1][20]['weight'] = 0.95
    print("second point: ", count_probability(model))
    draw_graph(model)

    model.add_edges_from([(1, 10), (5, 15)])
    model[1][10]['weight'] = 0.8
    model[5][15]['weight'] = 0.7
    print("third point: ", count_probability(model))
    draw_graph(model)

    r = [(random.randint(1, 20), random.randint(1, 20), 0.4) for i in range(0, 4)]
    model.add_weighted_edges_from(r)
    print(r)
    print("fourth point: ", count_probability(model))
    draw_graph(model)
