from input import input
import numpy as np

if __name__ == "__main__":
    max_pos = max(input)
    input_arr = np.array(input)
    arr = np.zeros(max_pos + 1, dtype=int)
    for e in input:
        arr[e] += 1

    min_val1 = float("inf")
    min_val2 = float("inf")
    for i in range(max_pos + 1):
        m1 = int(np.sum(np.abs(input_arr - i)))
        dist = np.abs(input_arr - i)
        m2 = int(np.sum(dist * (dist + 1) / 2))
        if m1 < min_val1:
            min_val1 = m1
        if m2 < min_val2:
            min_val2 = m2
    print(min_val1)
    print(min_val2)
