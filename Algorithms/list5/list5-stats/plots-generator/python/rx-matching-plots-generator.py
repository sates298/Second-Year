import json
import matplotlib.pyplot as plt
import math

def get_avg_time_by_i(actual_i):
    finalList = []
    avg = 0
    if(actual_i < 3):
        actual_k = 3
    else:
        actual_k = actual_i
    for data in stats:
        if data['i'] == actual_i:
            if data['k'] == actual_k:
                avg += data['time']
            else:
                avg = avg/main_r
                finalList.append(math.log2(avg))
                avg = data['time']
                actual_k = data['k']
    avg = avg/main_r
    finalList.append(math.log2(avg))
    return finalList


def get_avg_matching_by_k(actual_k):
    finalList = []
    avg = 0
    actual_i = 1
    for data in stats:
        if data['k'] == actual_k:
            if data['i'] == actual_i:
                avg += data['maxMatching']
            else:
                avg = avg/main_r
                finalList.append(math.log2(avg))
                avg = data['maxMatching']
                actual_i = data['i']
    avg = avg/main_r
    finalList.append(math.log2(avg))
    return finalList


def draw_matching_by_k_plot():
    for k in range(3,11):
        plt.plot([i for i in range(1, k+1)], get_avg_matching_by_k(k), label="k = " + str(k))

    plt.xlabel("i")
    plt.ylabel("max matching")
    plt.title("Max matching by i")
    plt.legend(loc='lower right')
    plt.show()


def draw_time_by_i_plot():
    for i in range(1,11):
        if i < 3:
            range_k = 3
        else:
            range_k = i
        plt.plot([k for k in range(range_k, 11)], get_avg_time_by_i(i), label="i = "+str(i))

    plt.xlabel("k")
    plt.ylabel("time")
    plt.legend(loc='upper left')
    plt.title("Time by k")
    plt.show()



def draw_all_plots():
    draw_time_by_i_plot()
    draw_matching_by_k_plot()


if __name__ == '__main__':
    main_r = 100
    fileRx = '../../matching' + str(main_r) + '.json'
    data = json.load(open(fileRx))
    stats = [i for i in data['all']]
    # print(get_avg_tim(3))
    draw_all_plots()
