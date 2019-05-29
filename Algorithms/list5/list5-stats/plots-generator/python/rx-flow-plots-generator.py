import json
import matplotlib.pyplot as plt
import math


def get_avg_values(data, r, what):
    final_list = []
    avg = 0
    actual_k = 1
    for i in data:
        if i['k'] == actual_k:
            avg = avg + i[what]
        else:
            avg = avg/r
            final_list.append({'k': actual_k, 'avg': math.log2(avg)})
            actual_k = i['k']
            avg = i[what]
    avg = avg/r
    final_list.append({'k': actual_k, 'avg': math.log2(avg)})
    return final_list


def get_time(data, r):
    return [i['avg'] for i in get_avg_values(data, r, 'time')]


def get_max_flow(data, r):
    return [i['avg'] for i in get_avg_values(data, r, 'maxFlow')]


def get_paths(data, r):
    return [i['avg'] for i in get_avg_values(data, r, 'paths')]


def draw_time_plot():
    plt.plot(ks, get_time(stats, main_r))
    plt.xlabel("dimension")
    plt.ylabel("Time")
    plt.title("plot of time")
    plt.show()


def draw_paths_plot():
    plt.plot(ks, get_paths(stats, main_r))
    plt.xlabel("dimension")
    plt.ylabel("Paths")
    plt.title("plot of number of paths")
    plt.show()


def draw_max_flow_plot():
    plt.plot(ks, get_max_flow(stats, main_r))
    plt.xlabel("dimension")
    plt.ylabel("Max Flow")
    plt.title("plot of maximum flow")
    plt.show()


def draw_all_plots():
    draw_time_plot()
    draw_max_flow_plot()
    draw_paths_plot()


if __name__ == '__main__':
    main_r = 101
    fileRx = '../../flow' + str(main_r) + '.json'
    data = json.load(open(fileRx))
    stats = [i for i in data['all']]
    ks = [i for i in range(1, 17)]
    print(get_time(stats, main_r))
    draw_all_plots()

