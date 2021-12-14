fun main() {
    fun preprocess(input: List<String>): Pair<String, Map<String, String>> {
        val rules = input.slice(2 until input.size).associate {
            val split = it.split(" -> ")
            split[0] to split[1]
        }
        return Pair(input[0], rules)
    }

    fun init(
        input: String,
        rules: Map<String, String>
    ): Triple<Map<String, List<String>>, Map<String, Long>, MutableMap<String, Long>> {
        val rulesProcessed = mutableMapOf<String, List<String>>()
        val counts = mutableMapOf<String, Long>()
        for ((k, v) in rules.entries) {
            val l = listOf(k[0] + v, v + k[1])
            rulesProcessed[k] = l
            counts[v] = 0L
            counts[k[0].toString()] = 0L
            counts[k[1].toString()] = 0L
        }
        counts[input[0].toString()] = 1
        val inputProcessed = mutableMapOf<String, Long>()
        for (i in 1 until input.length) {
            val needle = input[i - 1].toString() + input[i].toString()
            inputProcessed[needle] = inputProcessed.getOrDefault(needle, 0L) + 1L
            counts[input[i].toString()] = counts.getOrDefault(input[i].toString(), 0) + 1
        }

        return Triple(rulesProcessed, inputProcessed, counts)
    }

    fun step(
        inputProcessed: Map<String, Long>,
        rules: Map<String, String>,
        rulesProcessed: Map<String, List<String>>,
        counts: MutableMap<String, Long>
    ): Map<String, Long> {
        val inputProcessedNext = mutableMapOf<String, Long>()
        for ((k, l) in inputProcessed.entries) {
            rulesProcessed[k]?.forEach {
                inputProcessedNext[it] = inputProcessedNext.getOrDefault(it, 0L) + l
            }
            val added = rules[k]
            if (added != null) {
                counts[added] = counts.getOrDefault(added, 0) + l
            }
        }
        return inputProcessedNext
    }

    fun part1(input: String, rules: Map<String, String>): Long {
        var (rulesProcessed, inputProcessed, counts) = init(input, rules)
        for (i in 0 until 10) {
            inputProcessed = step(inputProcessed, rules, rulesProcessed, counts)
        }

        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    fun part2(input: String, rules: Map<String, String>): Long {
        var (rulesProcessed, inputProcessed, counts) = init(input, rules)
        for (i in 0 until 40) {
            inputProcessed = step(inputProcessed, rules, rulesProcessed, counts)
        }

        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    val (testInput, testRules) = preprocess(readInput(14, true))
    val (input, rules) = preprocess(readInput(14))

    check(part1(testInput, testRules) == 1588L)
    println(part1(input, rules))

    check(part2(testInput, testRules) == 2188189693529)
    println(part2(input, rules))
}
