fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var pos = 0
        for (e in input) {
            val num = e.last().toString().toInt()
            if (e.startsWith("forward")) {
                pos += num
            } else if (e.startsWith("down")) {
                depth += num
            } else {
                depth -= num
            }
        }
        return depth * pos
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var pos = 0
        var aim = 0
        for (e in input) {
            val num = e.last().toString().toInt()
            if (e.startsWith("forward")) {
                pos += num
                depth += aim * num
            } else if (e.startsWith("down")) {
                aim += num
            } else {
                aim -= num
            }
        }
        return depth * pos
    }

    val testInput = readInput(2, true)
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput(2)
    println(part1(input))
    println(part2(input))
}
