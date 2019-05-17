import json
import matplotlib.pyplot as plt

if __name__ == '__main__':
    main_r = 1
    fileRx = '../../k' + str(main_r) + '.json'
    data = json.load(open(fileRx))
    stats = [i for i in data['all']]