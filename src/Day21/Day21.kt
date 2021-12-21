class Dice(private val sides: Int) {
    var counter: Int = 0
        private set

    fun roll(): Int {
        var res = 0
        for (i in 1..3) {
            res += counter % sides + 1
            counter++
        }
        return res
    }
}

data class Position(val pos0: Int, val score0: Int, val pos1: Int, val score1: Int)

fun Position.add(player: Int, roll: Int): Position {
    return when (player) {
        0 -> {
            val pos0New = (pos0 + roll - 1) % 10 + 1
            Position(pos0New, score0 + pos0New, pos1, score1)
        }
        1 -> {
            val pos1New = (pos0 + roll - 1) % 10 + 1
            Position(pos0, score0, pos1New, score1 + pos1New)
        }
        else -> {
            throw IllegalArgumentException()
        }
    }
}

fun Position.swap() = Position(pos1, score1, pos0, score0)

fun main() {
    fun preprocess(input: List<String>): List<Int> {
        return input.map { it.split(" ").last().toInt() }
    }

    fun part1(input: List<Int>): Int {
        val die = Dice(100)
        var player = 0
        val pos = input.toMutableList()
        val score = mutableListOf(0, 0)
        while (score[1 - player] < 1000) {
            pos[player] = (pos[player] + die.roll() - 1) % 10 + 1
            score[player] += pos[player]
            player = (player + 1) % 2
        }
        return die.counter * score[player]
    }

    fun part2(input: List<Int>): Long {
        val intermediates = HashMap<Position, Array<Long>>()
        fun doStep(pos: Position): Array<Long> {
            intermediates[pos]?.also { return it }
            var res = arrayOf(0L, 0L)
            for (e0 in 1..3) for (e1 in 1..3) for (e2 in 1..3) {
                val diceCount = e0 + e1 + e2
                val nextPos = pos.add(0, diceCount)
                if (nextPos.score0 >= 21) {
                    res[0]++
                } else {
                    res = res.zip(doStep(nextPos.swap()).reversedArray()) { a, b -> a + b }.toTypedArray()
                }
            }
            intermediates[pos] = res
            return res
        }

        return doStep(Position(input[0], 0, input[1], 0)).maxOf { it }
    }

    val testInput = preprocess(readInput(21, true))
    val input = preprocess(readInput(21))

    check(part1(testInput) == 739785)
    println(part1(input))

    check(part2(testInput) == 444356092776315L)
    println(part2(input))
}
