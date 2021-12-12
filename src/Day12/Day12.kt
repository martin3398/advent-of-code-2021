import java.util.*

fun main() {
    fun preprocess(input: List<String>): Map<String, List<String>> {
        val res = mutableMapOf<String, MutableList<String>>()
        for (e in input) {
            val split = e.split('-')
            val l1 = res.getOrDefault(split[0], mutableListOf())
            l1.add(split[1])
            res[split[0]] = l1

            val l2 = res.getOrDefault(split[1], mutableListOf())
            l2.add(split[0])
            res[split[1]] = l2
        }
        return res
    }

    fun String.isUppercase(): Boolean = this.uppercase() == this

    fun calc(input: Map<String, List<String>>, selector: (List<String>, String) -> Boolean): Int {
        var res = 0
        val queue = LinkedList<List<String>>()
        queue.add(listOf("start"))
        while (queue.isNotEmpty()) {
            val cur = queue.pop()
            val next = input[cur.last()]
            if (next != null) {
                for (e in next) {
                    if (e == "end") {
                        res++
                    } else if (selector(cur, e)) {
                        val newCur = cur.toMutableList()
                        newCur.add(e)
                        queue.add(newCur)
                    }
                }
            }
        }
        return res
    }

    fun part1(input: Map<String, List<String>>): Int {
        return calc(input) { l, e -> e.isUppercase() || !l.contains(e) }
    }

    fun part2(input: Map<String, List<String>>): Int {
        return calc(input) { l, e ->
            val filtered = l.filter { !it.isUppercase() }
            val hasDouble = filtered.toSet().size != filtered.size
            val count = filtered.count { it == e }
            e != "start" && (e.isUppercase() || count == 0 || (!hasDouble && count < 2))
        }
    }

    val testInput = preprocess(readInput(12, true))
    val input = preprocess(readInput(12))

    check(part1(testInput) == 226)
    println(part1(input))

    check(part2(testInput) == 3509)
    println(part2(input))
}
