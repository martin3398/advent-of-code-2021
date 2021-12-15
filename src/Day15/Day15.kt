import java.util.*

class Node(val x: Int, val y: Int, val dist: Int) : Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return this.dist - other.dist
    }

    override fun toString(): String {
        return "($x, $y, $dist)"
    }
}

fun main() {
    fun pad(input: Array<Array<Int>>): Array<Array<Int>> {
        val parsed = input.map { it.toMutableList() }.toMutableList()
        val padding = MutableList(input[0].size) { Integer.MAX_VALUE }
        parsed.add(0, padding)
        parsed.add(padding)
        fun pad(x: List<Int>): Array<Int> {
            val l = x.toMutableList()
            l.add(0, Int.MAX_VALUE)
            l.add(Int.MAX_VALUE)
            return l.toTypedArray()
        }
        return parsed.map { pad(it) }.toTypedArray()
    }

    fun preprocess(input: List<String>): Array<Array<Int>> =
        input.map { it.map { x -> x.toString().toInt() }.toTypedArray() }.toTypedArray()


    fun dijkstra(input: Array<Array<Int>>): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = PriorityQueue<Node>()
        queue.add(Node(1, 1, 0))
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (!visited.contains(Pair(next.x, next.y))) {
                if (next.x == input.size - 2 && next.y == input.size - 2) {
                    return next.dist
                }
                queue.add(Node(next.x - 1, next.y, next.dist + input[next.x - 1][next.y]))
                queue.add(Node(next.x + 1, next.y, next.dist + input[next.x + 1][next.y]))
                queue.add(Node(next.x, next.y - 1, next.dist + input[next.x][next.y - 1]))
                queue.add(Node(next.x, next.y + 1, next.dist + input[next.x][next.y + 1]))
                visited.add(Pair(next.x, next.y))
            }
        }
        return -1
    }

    fun part1(input: Array<Array<Int>>): Int {
        return dijkstra(pad(input))
    }

    fun part2(input: Array<Array<Int>>): Int {
        val largeInput = input.map { it.toMutableList() }.toMutableList()
        for (i in 1 until 5) {
            largeInput.forEachIndexed { index, e -> e.addAll(input[index].map { ((it - 1 + i) % 9) + 1 }) }
        }
        val toAdd = largeInput.map { it.toMutableList() }.toMutableList()
        for (i in 1 until 5) {
            largeInput.addAll(toAdd.map { it.map { x -> ((x - 1 + i) % 9) + 1 }.toMutableList() })
        }
        return dijkstra(pad(largeInput.map { it.toTypedArray() }.toTypedArray()))
    }

    val testInput = preprocess(readInput(15, true))
    val input = preprocess(readInput(15))

    check(part1(testInput) == 40)
    println(part1(input))

    check(part2(testInput) == 315)
    println(part2(input))
}
