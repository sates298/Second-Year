import random
import networkx as nx


def create_graph(edges_no, p):
    graph = nx.Graph()
    graph.add_nodes_from([i for i in range(0, 10)])
    if edges_no < 9 or edges_no > 19:
        return None
    base_edges = [(i, i + 1, p) for i in range(0, 9)]
    graph.add_weighted_edges_from(base_edges)

    edges_no = edges_no - 9
    for i in range(edges_no):
        r1 = random.randint(0, 9)
        r2 = random.randint(0, 9)
        while graph.has_edge(r1, r2):
            r2 = random.randint(0, 9)
        graph.add_edge(r1, r2)
        graph[r1][r2]['weight'] = p

    return graph


def generate_n(bottom, top):
    for i in range(10):
        for j in range(10):
            if i != j:
                N[i][j] = random.randint(bottom, top)


def generate_c(graph):
    for i in range(10):
        for j in range(10):
            C[i][j] = 0
    for i in range(10):
        for j in range(10):
            if graph.has_edge(i, j):
                if c_mode == "maximum":
                    C[i][j] = 1000
                elif c_mode == "minimum":
                    C[i][j] = 100
                else:
                    C[i][j] = random.randint(100, 1000)


def generate_a(graph):
    for i in range(10):
        for j in range(10):
            A[i][j] = 0
    for i in range(10):
        for j in range(10):
            paths = list(nx.all_simple_paths(graph, i, j))
            weight = N[i][j]
            it = 0
            while it < len(paths) and not check_path(paths[it], weight):
                it += 1


def check_path(path, weight):
    for i in range(len(path)-1):
        if (get_a(path[i], path[i + 1]) + weight) > (get_c(path[i], path[i + 1])/avg_package_weight):
            return False
    for i in range(len(path) - 1):
        A[path[i]][path[i + 1]] += weight
    return True


def get_c(i, j):
    return C[i][j]


def get_a(i, j):
    return A[i][j]


def draw_matrix(matrix):
    for n in matrix:
        print(n)


N = [[0 for _ in range(10)] for _ in range(10)]
generate_n(1, 64)

network_model = create_graph(19, 0.5)
all_edges = [i for i in network_model.edges]

avg_package_weight = 64
T_max = 0.5

c_mode = "else"

C = [[0 for _ in range(10)] for _ in range(10)]
generate_c(network_model)
A = [[0 for _ in range(10)] for _ in range(10)]
generate_a(network_model)
