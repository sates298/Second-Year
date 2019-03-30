from packages_stream import *
from availability import draw_graph, random_remove
import copy


def count_g():
    s = 0
    for i in N:
        for j in i:
            s += j
    return s


def count_delay():
    s = 0
    for pair in all_edges:
        if ((get_c(*pair) / avg_package_weight) - get_a(*pair)) == 0 :
            return float("inf")
        else:
            s += (get_a(*pair) / ((get_c(*pair) / avg_package_weight) - get_a(*pair)))
    return s / count_g()


def count_t_probability():
    amount = 10000
    successes = 0
    for i in range(amount):
        clone = copy.deepcopy(network_model)
        random_remove(clone)
        generate_c(clone)
        generate_a(clone)
        if 0 < count_delay() < T_max:
            successes += 1

    return successes/amount


if __name__ == "__main__":
    print("N")
    draw_matrix(N)
    print("C")
    draw_matrix(C)
    print("A")
    draw_matrix(A)
    print(count_delay())
    # draw_graph(network_model)

    # print(count_t_probability())
