from input import input

if __name__ == "__main__":
    depth = 0
    pos = 0

    for e in input:
        num = int(e[-1])
        if e.startswith("forward"):
            pos += num
        elif e.startswith("down"):
            depth += num
        elif e.startswith("up"):
            depth -= num

    print(depth * pos)

    aim = 0
    depth = 0
    pos = 0

    for e in input:
        num = int(e[-1])
        if e.startswith("forward"):
            pos += num
            depth += aim * num
        elif e.startswith("down"):
            aim += num
        elif e.startswith("up"):
            aim -= num

    print(depth * pos)
