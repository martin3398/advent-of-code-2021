fun List<List<Char>>.print() {
    for (row in this) {
        for (e in row) {
            print(e)
        }
        println()
    }
}

fun main() {
    fun preprocess(input: List<String>): List<List<Char>> {
        return input.map { it.toList() }
    }

    fun doStep(input: List<List<Char>>): List<List<Char>> {
        val yLength = input.size
        val xLength = input[0].size
        val intermediate = input.map { it.toMutableList() }.toMutableList()
        for ((y, row) in input.withIndex()) {
            for ((x, e) in row.withIndex()) {
                if (e == '>') {
                    if (input[y][(x + 1) % xLength] == '.') {
                        intermediate[y][x] = '.'
                        intermediate[y][(x + 1) % xLength] = '>'
                    }
                }
            }
        }

        val intermediate2 = intermediate.map { it.toMutableList() }.toMutableList()
        for ((y, row) in input.withIndex()) {
            for ((x, e) in row.withIndex()) {
                if (e == 'v') {
                    if (intermediate[(y + 1) % yLength][x] == '.') {
                        intermediate2[y][x] = '.'
                        intermediate2[(y + 1) % yLength][x] = 'v'
                    }
                }
            }
        }
        return intermediate2
    }

    fun part1(input: List<List<Char>>): Int {
        var stepCount = 1
        var last = input
        var actual = doStep(input)

        while (last != actual) {
            last = actual
            actual = doStep(actual)
            stepCount++
        }
        return stepCount
    }

    val testInput = preprocess(readInput(25, true))
    val input = preprocess(readInput(25))

    check(part1(testInput) == 58)
    println(part1(input))
}
