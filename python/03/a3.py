from input import input

if __name__ == "__main__":
    gamma = 0
    eps = 0

    most_common = []
    least_common = []

    for i in range(12):
        zeros, ones = 0, 0
        for e in input:
            if e[i] == "1":
                ones += 1
            else:
                zeros += 1
        if zeros > ones:
            gamma += 2 ** (11 - i)
        else:
            eps += 2 ** (11 - i)
        most_common.append("0" if zeros > ones else "1")
        least_common.append("1" if zeros > ones else "0")

    print(gamma * eps)

    def count_at_pos(l, i):
        ones, zeros = [], []
        for e in l:
            if e[i] == "0":
                zeros.append(e)
            else:
                ones.append(e)
        return zeros, ones

    oxygen = input.copy()

    for i in range(12):
        zeros, ones = count_at_pos(oxygen, i)
        if len(ones) >= len(zeros):
            oxygen = ones
        else:
            oxygen = zeros
        if len(oxygen) == 1:
            break

    co2 = input.copy()

    for i in range(12):
        zeros, ones = count_at_pos(co2, i)
        if len(ones) >= len(zeros):
            co2 = zeros
        else:
            co2 = ones
        if len(co2) == 1:
            break

    print(int(oxygen[0], 2) * int(co2[0], 2))
