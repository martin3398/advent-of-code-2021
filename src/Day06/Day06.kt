fun main() {
    fun calc(input: List<Int>, iterations: Int): Long {
        val counts = MutableList<Long>(9) { 0 }
        input.forEach { counts[it]++ }
        for (i in 0 until iterations) {
            val numNew = counts.removeFirst()
            counts[6] += numNew
            counts.add(numNew)
        }

        return counts.sum()
    }

    fun part1(input: List<Int>): Long {
        return calc(input, 80)
    }

    fun part2(input: List<Int>): Long {
        return calc(input, 256)
    }

    val testInput = readInput(6, true)[0].split(',').map { it.toInt() }
    val input = readInput(6)[0].split(',').map { it.toInt() }

    check(part1(testInput) == 5934L)
    println(part1(input))

    check(part2(testInput) == 26984457539)
    println(part2(input))
}
