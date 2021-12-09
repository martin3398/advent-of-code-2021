import java.lang.Integer.MAX_VALUE

fun main() {
    fun preprocess(input: List<String>): List<List<Int>> {
        val parsed = input.map { x -> x.map { it.toString().toInt() } }.toMutableList()
        val padding = MutableList(input[0].length) { MAX_VALUE }
        parsed.add(0, padding)
        parsed.add(padding)
        fun pad(x: List<Int>): List<Int> {
            val l = x.toMutableList()
            l.add(0, Int.MAX_VALUE)
            l.add(Int.MAX_VALUE)
            return l
        }
        return parsed.map { pad(it) }
    }

    fun part1(input: List<List<Int>>): Int {
        var res = 0
        for (x in 1..input.size - 2) {
            for (y in 1..input[x].size - 2) {
                if (input[x][y] < input[x + 1][y]
                    && input[x][y] < input[x - 1][y]
                    && input[x][y] < input[x][y + 1]
                    && input[x][y] < input[x][y - 1]
                ) {
                    res += 1 + input[x][y]
                }
            }
        }
        return res
    }

    fun part2(input: List<List<Int>>): Int {
        return 0
    }

    val testInput = preprocess(readInput(9, true))
    val input = preprocess(readInput(9))

    check(part1(testInput) == 15)
    println(part1(input))

    check(part2(testInput) == 61229)
    println(part2(input))
}
