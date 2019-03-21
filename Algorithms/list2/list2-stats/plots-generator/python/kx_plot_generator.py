import json
import matplotlib.pyplot as plt


def get_avg_value(sort_data, k, what):
    final_list = []
    avg = 0
    for i in sort_data:
        if i['k'] < k:
            avg = avg + i['sort'][what]
        else:
            avg = avg + i['sort'][what]
            avg = avg/k
            final_list.append({'n': i['n'], 'avg': avg})
            avg = 0
    return final_list


def get_compares(sort_data, k):
    return [i['avg'] for i in get_avg_value(sort_data, k, 'compares')]


def get_swaps(sort_data, k):
    return [i['avg'] for i in get_avg_value(sort_data, k, 'swaps')]


def get_time(sort_data, k):
    return [i['avg'] for i in get_avg_value(sort_data, k, 'time')]


def get_compares_n(sort_data, k):
    return [i['avg']/i['n'] for i in get_avg_value(sort_data, k, 'compares')]


def get_swaps_n(sort_data, k):
    return [i['avg']/i['n'] for i in get_avg_value(sort_data, k, 'swaps')]


def draw_compare_plot(yrange, k):
    plt.plot(ns, get_compares(insertSorts, k), label='Insert')
    plt.plot(ns, get_compares(selectSorts, k), label='Select')
    plt.plot(ns, get_compares(heapSorts, k), label="Heap")
    plt.plot(ns, get_compares(quickSorts, k), label="Quick")
    plt.plot(ns, get_compares(mQuickSorts, k), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Compares")
    plt.legend(loc='upper left')
    plt.ylim(0, yrange)
    plt.title('Comparisons when k = ' + str(k))
    plt.show()


def draw_swap_plot(yrange, k):
    plt.plot(ns, get_swaps(insertSorts, k), label='Insert')
    plt.plot(ns, get_swaps(selectSorts, k), label='Select')
    plt.plot(ns, get_swaps(heapSorts, k), label="Heap")
    plt.plot(ns, get_swaps(quickSorts, k), label="Quick")
    plt.plot(ns, get_swaps(mQuickSorts, k), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Swaps")
    plt.legend(loc='upper left')
    plt.ylim(0, yrange)
    plt.title('Changes keys when k = ' + str(k))
    plt.show()


def draw_time_plot(yrange, k):
    plt.plot(ns, get_time(insertSorts, k), label='Insert')
    plt.plot(ns, get_time(selectSorts, k), label='Select')
    plt.plot(ns, get_time(heapSorts, k), label="Heap")
    plt.plot(ns, get_time(quickSorts, k), label="Quick")
    plt.plot(ns, get_time(mQuickSorts, k), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Time [ms]")
    plt.legend(loc='upper left')
    plt.ylim(0, yrange)
    plt.title('Time when k = ' + str(k))
    plt.show()


def draw_compare_n_plot(yrange, k):
    plt.plot(ns, get_compares_n(insertSorts, k), label='Insert')
    plt.plot(ns, get_compares_n(selectSorts, k), label='Select')
    plt.plot(ns, get_compares_n(heapSorts, k), label="Heap")
    plt.plot(ns, get_compares_n(quickSorts, k), label="Quick")
    plt.plot(ns, get_compares_n(mQuickSorts, k), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Compares/n")
    plt.legend(loc='upper left')
    plt.ylim(0, yrange)
    plt.title('Compares/n when k = ' + str(k))
    plt.show()


def draw_swap_n_plot(yrange, k):
    plt.plot(ns, get_swaps_n(insertSorts, k), label='Insert')
    plt.plot(ns, get_swaps_n(selectSorts, k), label='Select')
    plt.plot(ns, get_swaps_n(heapSorts, k), label="Heap")
    plt.plot(ns, get_swaps_n(quickSorts, k), label="Quick")
    plt.plot(ns, get_swaps_n(mQuickSorts, k), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Swaps/n")
    plt.legend(loc='upper left')
    plt.ylim(0, yrange)
    plt.title('Swaps/n when k = ' + str(k))
    plt.show()


def draw_all_plots_by_k(k):
    draw_compare_plot(500000, k)
    draw_compare_plot(60000000, k)
    draw_swap_plot(300000, k)
    draw_swap_plot(30000000, k)
    draw_time_plot(5, k)
    draw_time_plot(190, k)
    draw_compare_n_plot(100, k)
    draw_compare_n_plot(6000, k)
    draw_swap_n_plot(60, k)
    draw_swap_n_plot(3000, k)


if __name__ == '__main__':
    main_k = 1000  # it's working only for k = 1, 10,100 or 1000
    fileKx = '../../k' + str(main_k) + '.json'
    data = json.load(open(fileKx))
    sorts = [i for i in data['all']]

    insertSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][0]} for i in sorts]
    selectSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][1]} for i in sorts]
    heapSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][2]} for i in sorts]
    quickSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][3]} for i in sorts]
    mQuickSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][4]} for i in sorts]

    ns = [i*100 for i in range(1, 101)]

    draw_all_plots_by_k(main_k)

