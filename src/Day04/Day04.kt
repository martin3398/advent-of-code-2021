import java.lang.IllegalStateException

fun main() {
    fun prepare(input: List<String>): Pair<List<Int>, List<List<List<Int>>>> {
        val fst = input[0].split(',').map { it.toInt() }
        val snd = arrayListOf<List<List<Int>>>()
        val res = input.slice(2 until input.size)
        var remainder = arrayListOf<List<Int>>()
        for (e in res) {
            if (e == "") {
                snd.add(remainder)
                remainder = arrayListOf()
            } else {
                remainder.add(e.trim().replace("\\s+".toRegex(), " ").split(" ").map { it.toInt() })
            }
        }
        snd.add(remainder)
        return Pair(fst, snd)
    }

    fun part1(sequence: List<Int>, grids: List<List<List<Int>>>): Int {
        var gridsWc = grids
        for (e in sequence) {
            gridsWc = gridsWc.map { e1 -> e1.map { e2 -> e2.map { if (it == e) 0 else it } } }
            for (x in gridsWc) {
                for (i in 0 until 5) {
                    if (x[i].sum() == 0) {
                        return x.sumOf { it.sum() } * e
                    }
                    if (x.mapIndexed { index, list -> if (index == i) list.sum() else 0 }.sum() == 0) {
                        return x.sumOf { it.sum() } * e
                    }
                }
            }
        }
        throw IllegalStateException("Nobody won")
    }

    fun part2(sequence: List<Int>, grids: List<List<List<Int>>>): Int {
        var gridsWc = grids
        var loser = -1
        for (e in sequence) {
            gridsWc = gridsWc.map { e1 -> e1.map { e2 -> e2.map { if (it == e) 0 else it } } }
            var loseCount = 0
            for ((gridIndex, x) in gridsWc.withIndex()) {
                var won = false
                for (i in 0 until 5) {
                    if (x[i].sum() == 0) {
                        won = true
                        break
                    }
                    if (x.sumOf { it.filterIndexed { index, _ -> index == i }[0] } == 0) {
                        won = true
                        break
                    }
                }
                if (!won) {
                    loseCount++
                    loser = gridIndex
                }
            }
            if (loseCount == 0) {
                return gridsWc[loser].sumOf { it.sum() } * e
            }
        }
        throw IllegalStateException("Nobody won last")
    }

    val (testSequence, testGrids) = prepare(readInput(4, true))
    val (sequence, grids) = prepare(readInput(4))

    check(part1(testSequence, testGrids) == 4512)
    println(part1(sequence, grids))

    check(part2(testSequence, testGrids) == 1924)
    println(part2(sequence, grids))
}
