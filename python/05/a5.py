import numpy as np
from input import input

input_working_copy = [
    [list(map(int, a.split(","))) for a in x.split(" -> ")] for x in input
]

max_arr = np.max(input_working_copy) + 1
array = np.zeros((max_arr, max_arr), dtype=int)

for e in input_working_copy:
    if e[0][0] == e[1][0] or e[0][1] == e[1][1]:
        array[
            min(e[0][0], e[1][0]) : max(e[0][0], e[1][0]) + 1,
            min(e[0][1], e[1][1]) : max(e[0][1], e[1][1]) + 1,
        ] += 1

print(np.sum(array > 1))


array = np.zeros((max_arr, max_arr), dtype=int)

for e in input_working_copy:
    if e[0][0] == e[1][0] or e[0][1] == e[1][1]:
        array[
            min(e[0][0], e[1][0]) : max(e[0][0], e[1][0]) + 1,
            min(e[0][1], e[1][1]) : max(e[0][1], e[1][1]) + 1,
        ] += 1
    else:
        indizes = np.array(
            [
                [
                    e[0][0] + (i if e[0][0] < e[1][0] else -i),
                    e[0][1] + (i if e[0][1] < e[1][1] else -i),
                ]
                for i in range(max(e[0][0], e[1][0]) - min(e[0][0], e[1][0]) + 1)
            ]
        ).T
        array[indizes[0], indizes[1]] += 1

print(np.sum(array > 1))
