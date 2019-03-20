import matplotlib.pyplot as plt
import pandas as pd
import json


def get_compares(x):
    return [i['sort']['compares'] for i in x]


if __name__ == '__main__':
    fileK1 = '../../list2-stats/k1.json'
    data = json.load(open(fileK1))
    sorts = [i for i in data['all']]

    insertSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][0]} for i in sorts]
    selectSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][1]} for i in sorts]
    heapSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][2]} for i in sorts]
    quickSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][3]} for i in sorts]
    mQuickSorts = [{'k': i['k'], 'n': i['n'], 'sort':i['sorts'][4]} for i in sorts]

    ns = [i['n'] for i in insertSorts]

    plt.plot(ns, get_compares(insertSorts), label='Insert')
    plt.plot(ns, get_compares(selectSorts), label='Select')
    plt.plot(ns, get_compares(heapSorts), label="Heap")
    plt.plot(ns, get_compares(quickSorts), label="Quick")
    plt.plot(ns, get_compares(mQuickSorts), label="MQuick")
    plt.xlabel("n")
    plt.ylabel("Compares [mln]")
    plt.legend(loc='upper left')
    plt.ylim(0, 60000000)
    plt.title('Comparisons when k = 1')
    plt.show()


