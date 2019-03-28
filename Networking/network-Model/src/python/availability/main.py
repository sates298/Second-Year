from availability import *


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
