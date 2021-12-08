fun main() {
    fun preprocess(input: List<String>): List<Pair<List<String>, List<String>>> {
        fun preprocessSingle(input: String): Pair<List<String>, List<String>> {
            val split = input.split('|')
            val fst = split[0].split(' ').filter { it.isNotBlank() }
            val snd = split[1].split(' ').filter { it.isNotBlank() }
            return Pair(fst, snd)
        }
        return input.map { preprocessSingle(it) }
    }

    fun part1(input: List<Pair<List<String>, List<String>>>): Int {
        return input.map { it.second }.sumOf { it.count { e -> listOf(2, 3, 4, 7).contains(e.length) } }
    }

    fun part2(input: List<Pair<List<String>, List<String>>>): Int {
        fun processSingle(line: Pair<List<String>, List<String>>): Int {
            val second = line.second.map { it.toSet() }
            val all = line.first + line.second
            val one = all.filter { it.length == 2 }[0].toSet()
            val four = all.filter { it.length == 4 }[0].toSet()
            val known = mapOf(Pair(2, "1"), Pair(3, "7"), Pair(4, "4"), Pair(7, "8"))
            var res = ""
            for (e in second) {
                res += if (e.size in known.keys) {
                    known[e.size]
                } else if (e.size == 5) {
                    if (e.intersect(one).size == 2) {
                        "3"
                    } else if (e.intersect(four).size == 2) {
                        "2"
                    } else {
                        "5"
                    }
                } else {
                    if (e.intersect(one).size == 1) {
                        "6"
                    } else if (e.intersect(four).size == 4) {
                        "9"
                    } else {
                        "0"
                    }
                }
            }
            return res.toInt()
        }
        return input.sumOf { processSingle(it) }
    }

    val testInput = preprocess(readInput(8, true))
    val input = preprocess(readInput(8))

    check(part1(testInput) == 26)
    println(part1(input))

    check(part2(testInput) == 61229)
    println(part2(input))
}
