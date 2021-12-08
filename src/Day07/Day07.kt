import java.lang.Integer.MAX_VALUE
import kotlin.math.abs

fun main() {
    fun calc(input: List<Int>, fuelFunc: (List<Int>, Int) -> Int): Int {
        val maxIn = input.maxOf { it }

        var minVal = MAX_VALUE
        for (i in 0..maxIn) {
            val m = fuelFunc(input, i)
            if (m < minVal) {
                minVal = m
            }
        }
        return minVal
    }

    fun part1(input: List<Int>): Int {
        return calc(input) { l, i -> l.sumOf { abs(it - i) } }
    }

    fun part2(input: List<Int>): Int {
        return calc(input) { l, i ->
            l.sumOf {
                val dist = abs(it - i)
                dist * (dist + 1) / 2
            }
        }
    }

    val testInput = readInput(7, true)[0].split(',').map { it.toInt() }
    val input = readInput(7)[0].split(',').map { it.toInt() }

    check(part1(testInput) == 37)
    println(part1(input))

    check(part2(testInput) == 168)
    println(part2(input))
}
