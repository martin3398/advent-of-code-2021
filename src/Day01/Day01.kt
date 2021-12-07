fun main() {
    fun part1(input: List<Int>): Int {
        var res = 0
        var last = input[0]
        for (e in input) {
            if (e > last)
                res++
            last = e
        }
        return res
    }

    fun part2(input: List<Int>): Int {
        var res = 0
        for (i in 0 until input.size - 3) {
            if (input.slice(i until i + 3).sum() < input.slice(i + 1 until i + 4).sum())
                res++
        }
        return res
    }

    val testInput = readInput(1, true).map { it.toInt() }
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput(1).map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
