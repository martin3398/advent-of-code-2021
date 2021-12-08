import kotlin.math.min
import kotlin.math.max

fun main() {
    fun prepare(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        return input.map {
            val split = it.split("->").map { x -> x.split(',').map { e -> e.trim() } }
            Pair(Pair(split[0][0].toInt(), split[0][1].toInt()), Pair(split[1][0].toInt(), split[1][1].toInt()))
        }
    }

    fun part1(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        val inputWc = input.filter { it.first.first == it.second.first || it.first.second == it.second.second }
        val maxVal =
            input.maxOf { max(max(it.first.first, it.first.second), max(it.second.first, it.second.second)) } + 1
        val array = MutableList(maxVal) { MutableList(maxVal) { 0 } }
        for (e in inputWc) {
            array.subList(min(e.first.first, e.second.first), max(e.first.first, e.second.first) + 1)
                .map { it.subList(min(e.first.second, e.second.second), max(e.first.second, e.second.second) + 1) }
                .forEach { it.forEachIndexed { index2, i -> it[index2] = i + 1 } }
        }
        return array.sumOf { x -> x.count { it > 1 } }
    }

    fun part2(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        val maxVal =
            input.maxOf { max(max(it.first.first, it.first.second), max(it.second.first, it.second.second)) } + 1
        val array = MutableList(maxVal) { MutableList(maxVal) { 0 } }
        for (e in input) {
            if (e.first.first == e.second.first || e.first.second == e.second.second) {
                array.subList(min(e.first.first, e.second.first), max(e.first.first, e.second.first) + 1)
                    .map { it.subList(min(e.first.second, e.second.second), max(e.first.second, e.second.second) + 1) }
                    .forEach { it.forEachIndexed { index2, i -> it[index2] = i + 1 } }
            } else {
                val length = e.first.first - e.second.first
                val range = if (length > 0) 0..length else 0..-length
                for (i in range) {
                    array[e.first.first + if (e.first.first > e.second.first) -i else i][e.first.second + if (e.first.second > e.second.second) -i else i]++
                }
            }
        }
        return array.sumOf { x -> x.count { it > 1 } }
    }

    val testInput = prepare(readInput(5, true))
    val input = prepare(readInput(5))

    check(part1(testInput) == 5)
    println(part1(input))

    check(part2(testInput) == 12)
    println(part2(input))
}
