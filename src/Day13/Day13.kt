import kotlin.math.max

fun main() {
    fun Array<Array<Boolean>>.print() {
        for (row in this) {
            for (e in row) {
                print("${if (e) "X" else "."} ")
            }
            println()
        }
    }

    fun preprocess(input: List<String>): Pair<Array<Array<Boolean>>, List<Pair<String, Int>>> {
        var i = 0
        val points = mutableListOf<Pair<Int, Int>>()
        while (input[i] != "") {
            val split = input[i].split(",")
            points.add(Pair(split[0].toInt(), split[1].toInt()))
            i++
        }
        i++
        val folds = mutableListOf<Pair<String, Int>>()
        while (i < input.size) {
            val split = input[i].split(" ").last().split("=")
            folds.add(Pair(split[0], split[1].toInt()))
            i++
        }

        val maxX = points.maxOf { it.first }
        val maxY = points.maxOf { it.second }
        val grid = Array(maxY + 1) { Array(maxX + 1) { false } }
        for (e in points) {
            grid[e.second][e.first] = true
        }
        return Pair(grid, folds)
    }

    fun foldY(input: Array<Array<Boolean>>, direction: Int): Array<Array<Boolean>> {
        var lower = input.slice(0 until direction).toTypedArray()
        var upper = input.slice(direction + 1 until input.size).reversed().toTypedArray()
        if (lower.size > upper.size) {
            lower = upper.also { upper = lower }
        }
        val res = Array(max(lower.size, upper.size)) { Array(input[0].size) { false } }
        for ((i, e) in upper.withIndex()) {
            res[i] = e
        }
        val offset = res.size - lower.size
        for ((i, e) in lower.withIndex()) {
            res[offset + i] = res[offset + i].mapIndexed { index, b -> b || e[index] }.toTypedArray()
        }
        return res
    }

    fun foldX(input: Array<Array<Boolean>>, direction: Int): Array<Array<Boolean>> {
        var left = input.map { it.slice(0 until direction).toTypedArray() }.toTypedArray()
        var right = input.map { it.slice(direction + 1 until it.size).reversed().toTypedArray() }.toTypedArray()
        if (left[0].size > right[0].size) {
            right = left.also { left = right }
        }
        val res = Array(input.size) { Array(max(left[0].size, right[0].size)) { false } }
        for ((i, e) in right.withIndex()) {
            res[i] = e
        }
        val offset = res[0].size - left[0].size
        for ((i, e) in left.withIndex()) {
            res[i] =
                res[i].mapIndexed { index, b -> b || if (index >= offset) e[index - offset] else false }.toTypedArray()
        }
        return res
    }

    fun part1(input: Pair<Array<Array<Boolean>>, List<Pair<String, Int>>>): Int {
        return if (input.second[0].first == "x") {
            foldX(input.first, input.second[0].second)
        } else {
            foldY(input.first, input.second[0].second)
        }.sumOf { it.count { x -> x } }
    }

    fun part2(input: Pair<Array<Array<Boolean>>, List<Pair<String, Int>>>): Int {
        var inputWc = input.first
        for (e in input.second) {
            inputWc = if (e.first == "x") {
                foldX(inputWc, e.second)
            } else {
                foldY(inputWc, e.second)
            }
        }
        inputWc.print()
        return 0
    }

    val testInput = preprocess(readInput(13, true))
    val input = preprocess(readInput(13))

    check(part1(testInput) == 17)
    println(part1(input))

    check(part2(testInput) == 0)
    println(part2(input))
}
