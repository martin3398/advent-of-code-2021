from input import input as input


def initalize(input: list[int]) -> list[int]:
    counts = 9 * [0]
    for e in input:
        counts[e] += 1
    return counts


def simulate_step(counts: list[int]) -> list[int]:
    num_new = counts.pop(0)
    counts[6] += num_new
    counts.append(num_new)
    return counts


if __name__ == "__main__":
    counts = 9 * [0]
    for e in input:
        counts[e] += 1
    for _ in range(80):
        input_arr = simulate_step(counts)
    print(sum(counts))
    for _ in range(256 - 80):
        input_arr = simulate_step(counts)
    print(sum(counts))
