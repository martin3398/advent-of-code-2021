import java.util.*

fun main() {
    fun preprocess(input: List<String>) =
        input.map { it.map { x -> x.toString().toInt() }.toTypedArray() }.toTypedArray()

    fun simulateStep(input: Array<Array<Int>>): Int {
        var lights = 0
        val queue = LinkedList<Pair<Int, Int>>()
        for (x in input.indices) {
            for (y in input.indices) {
                queue.add(x to y)
            }
        }
        while (!queue.isEmpty()) {
            val coords = queue.removeFirst()
            if (coords.first >= 0 && coords.first < input.size && coords.second >= 0 && coords.second < input[0].size) {
                input[coords.first][coords.second]++
                if (input[coords.first][coords.second] == 10) {
                    lights++
                    queue.addAll(
                        listOf(
                            coords.first - 1 to coords.second,
                            coords.first - 1 to coords.second - 1,
                            coords.first - 1 to coords.second + 1,
                            coords.first to coords.second - 1,
                            coords.first to coords.second + 1,
                            coords.first + 1 to coords.second,
                            coords.first + 1 to coords.second - 1,
                            coords.first + 1 to coords.second + 1
                        )
                    )
                }
            }
        }

        for (x in input.indices) {
            for (y in input.indices) {
                if (input[x][y] > 9) {
                    input[x][y] = 0
                }
            }
        }

        return lights
    }

    fun part1(input: Array<Array<Int>>): Int {
        var res = 0
        for (i in 0 until 100) {
            res += simulateStep(input)
        }
        return res
    }

    fun part2(input: Array<Array<Int>>): Int {
        var i = 101
        while (simulateStep(input) < 100) {
            i++
        }
        return i
    }

    val testInput = preprocess(readInput(11, true))
    val input = preprocess(readInput(11))

    check(part1(testInput) == 1656)
    println(part1(input))

    check(part2(testInput) == 195)
    println(part2(input))
}
