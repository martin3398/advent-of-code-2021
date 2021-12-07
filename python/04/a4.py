import numpy as np
from input import sequence, grid

grid_working_copy = [list(map(int, x.split())) for x in grid if x != ""]

grid_working_copy = np.array(grid_working_copy).reshape((-1, 5, 5))


def a1(grid):
    for e in sequence:
        grid[grid == e] = 0
        for x in range(np.shape(grid)[0]):
            for i in range(5):
                if np.all(grid[x, i, :] == 0):
                    print(np.sum(grid[x]) * e)
                    return
                if np.all(grid[x, :, i] == 0):
                    print(np.sum(grid[x]) * e)
                    return


a1(grid_working_copy)

grid_working_copy = [list(map(int, x.split())) for x in grid if x != ""]

grid_working_copy = np.array(grid_working_copy).reshape((-1, 5, 5))

for e in sequence:
    grid_working_copy[grid_working_copy == e] = 0
    lose_count = 0
    loser = -1
    for x in range(np.shape(grid_working_copy)[0]):
        won = False
        for i in range(5):
            if np.all(grid_working_copy[x, i, :] == 0):
                won = True
                break
            elif np.all(grid_working_copy[x, :, i] == 0):
                won = True
                break
        if not won:
            lose_count += 1
            loser = x
    if lose_count == 0:
        print(np.sum(grid_working_copy[loser]) * e)
        exit()
