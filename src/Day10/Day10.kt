import java.util.*

fun main() {
    val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val opening = setOf('(', '[', '{', '<')
    val closing = setOf(')', ']', '}', '>')
    val openingFrom = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    val closingFrom = mapOf('(' to 1, '[' to  2, '{' to 3, '<' to 4)

    fun calcScore1(line: String): Int {
        val queue = LinkedList<Char>()
        for (e in line) {
            if (opening.contains(e)) {
                queue.add(e)
            } else if (closing.contains(e)) {
                if (queue.removeLast() != openingFrom[e]) {
                    return scores.getOrDefault(e, 0)
                }
            }
        }
        return 0
    }

    fun calcScore2(line: String): Long {
        val queue = LinkedList<Char>()
        for (e in line) {
            if (opening.contains(e)) {
                queue.add(e)
            } else if (closing.contains(e)) {
                queue.removeLast()
            }
        }
        var res: Long = 0
        while (!queue.isEmpty()) {
            val next = queue.removeLast()
            res *= 5
            res += closingFrom.getOrDefault(next, 0)
        }
        return res
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { calcScore1(it) }
    }

    fun part2(input: List<String>): Long {
        val inputWc = input.filter { calcScore1(it) == 0 }
        return inputWc.map { calcScore2(it) }.sorted()[inputWc.size / 2]
    }

    val testInput = readInput(10, true)
    val input = readInput(10)

    check(part1(testInput) == 26397)
    println(part1(input))

    check(part2(testInput) == 288957L)
    println(part2(input))
}
