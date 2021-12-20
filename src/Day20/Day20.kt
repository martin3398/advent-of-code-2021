fun Array<Array<Boolean>>.print() {
    for (row in this) {
        row.print()
    }
}

fun Array<Boolean>.print() {
    for (e in this) {
        print(if (e) "#" else ".")
    }
    println()
}

fun Boolean.toInt() = if (this) 1 else 0

fun main() {
    val paddingSize = 200

    fun preprocess(input: List<String>): Pair<Array<Array<Boolean>>, Array<Boolean>> {
        val algorithm = input[0].map { it == '#' }.toTypedArray()
        val ySize = input.size - 2 + 2 * paddingSize
        val xSize = input[2].length + 2 * paddingSize
        val res = Array<Array<Boolean>>(ySize) { Array(xSize) { false } }
        for (i in 0 until paddingSize) {
            res[i] = Array(xSize) { false }
            res[ySize - 1 - i] = Array(xSize) { false }
        }
        for ((i, e) in input.drop(2).withIndex()) {
            res[i + paddingSize] =
                Array(paddingSize) { false } + e.map { it == '#' }.toTypedArray() + Array(paddingSize) { false }
        }
        return Pair(res, algorithm)
    }

    fun applyAlgorithm(input: Array<Array<Boolean>>, algorithm: Array<Boolean>): Array<Array<Boolean>> {
        val res = Array(input.size) { Array(input[0].size) { false } }

        for (y in 1 until input.size - 1) {
            for (x in 1 until input[0].size - 1) {
                val bits = input[y - 1].sliceArray(x - 1..x + 1) +
                        input[y].sliceArray(x - 1..x + 1) +
                        input[y + 1].sliceArray(x - 1..x + 1)
                val index = bits.joinToString("") { it.toInt().toString() }.toInt(2)
                res[y][x] = algorithm[index]
            }
        }
        return res
    }

    fun iterate(input: Array<Array<Boolean>>, algorithm: Array<Boolean>, num: Int): Int {
        var intermediate = input
        for (i in 0 until num) {
            intermediate = applyAlgorithm(intermediate, algorithm)
        }
        intermediate = intermediate.map { it.sliceArray(num until it.size - num) }
            .slice(num until intermediate.size - num).toTypedArray()
        return intermediate.sumOf { it.count { x -> x } }
    }

    fun part1(input: Array<Array<Boolean>>, algorithm: Array<Boolean>): Int {
        return iterate(input, algorithm, 2)
    }

    fun part2(input: Array<Array<Boolean>>, algorithm: Array<Boolean>): Int {
        return iterate(input, algorithm, 50)
    }

    val (testInput, testAlgorithm) = preprocess(readInput(20, true))
    val (input, algorithm) = preprocess(readInput(20))

    check(part1(testInput, testAlgorithm) == 35)
    println(part1(input, algorithm))

    check(part2(testInput, testAlgorithm) == 3351)
    println(part2(input, algorithm))
}
