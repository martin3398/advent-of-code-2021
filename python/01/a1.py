from input import input

if __name__ == "__main__":
    output = 0
    for i in range(len(input) - 1):
        if input[i] > input[i - 1]:
            output += 1

    print(output)

    output = 0
    for i in range(len(input) - 3):
        if sum(input[i : i + 3]) < sum(input[i + 1 : i + 4]):
            output += 1

    print(output)
