import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.abs

data class GamePosition(val rooms: Array<Array<Char?>>, val hallway: Array<Char?>) {
    private fun moveToHallway(room: Int, hallwayPos: Int): GamePosition {
        val rooms = this.rooms.map { it.copyOf() }.toTypedArray()
        val hallway = this.hallway.copyOf()

        val firstPod = getFirstOccupiedPos(room)!!
        hallway[hallwayPos] = rooms[room][firstPod]
        rooms[room][firstPod] = null

        return GamePosition(rooms, hallway)
    }

    private fun moveToRoom(hallwayPos: Int, room: Int): GamePosition {
        val rooms = this.rooms.map { it.copyOf() }.toTypedArray()
        val hallway = this.hallway.copyOf()

        val lastFreePod = getFirstEmptyPos(room)!!
        rooms[room][lastFreePod] = hallway[hallwayPos]
        hallway[hallwayPos] = null

        return GamePosition(rooms, hallway)
    }

    private fun getFirstEmptyPos(room: Int) = rooms[room].withIndex().lastOrNull { it.value == null }?.index

    private fun getFirstOccupiedPos(room: Int) = rooms[room].withIndex().firstOrNull { it.value != null }?.index

    private fun isRoomEmpty(room: Int) = rooms[room].all { it == null }

    private fun getFreeHallwayPositions(room: Int): Set<Int> {
        val before = (0 until roomHallwayPos[room]).toList().takeLastWhile { hallway[it] == null }
        val after = (roomHallwayPos[room] + 1..10).takeWhile { hallway[it] == null }
        val res = buildSet<Int> {
            addAll(before)
            addAll(after)
            removeIf { it != 0 && it != 1 && it != 9 && it != 10 && it % 2 == 0 }
        }
        return res
    }

    fun successors(): Set<Pair<GamePosition, Int>> {
        val res = mutableSetOf<Pair<GamePosition, Int>>()
        for ((roomId, room) in rooms.withIndex()) {
            if (!isRoomEmpty(roomId)) {
                val roomPos = getFirstOccupiedPos(roomId)!!
                val pod = room[roomPos]!!
                val targetPod = reversedRoomMapping[roomId]
                if (rooms[roomId].any { it != targetPod && it != null }) {
                    for (hallwayPos in getFreeHallwayPositions(roomId)) {
                        val dist = roomPos + 1 + abs(hallwayPos - roomHallwayPos[roomId])
                        res.add(moveToHallway(roomId, hallwayPos) to dist * weights[pod]!!)
                    }
                }
            }
        }
        for ((hallwayPos, pod) in hallway.withIndex()) {
            if (pod != null) {
                val targetRoom = roomMapping[pod]!!
                val targetHallwayPos = roomHallwayPos[targetRoom]
                val hallwaySection = if (targetHallwayPos > hallwayPos) {
                    hallwayPos + 1..targetHallwayPos
                } else {
                    targetHallwayPos until hallwayPos
                }
                if (hallwaySection.all { hallway[it] == null } && rooms[targetRoom].all { it == null || it == pod }) {
                    val dist = getFirstEmptyPos(targetRoom)!! + 1 + abs(hallwayPos - targetHallwayPos)
                    res.add(moveToRoom(hallwayPos, targetRoom) to dist * weights[pod]!!)
                }
            }
        }
        return res
    }

    override fun toString(): String {
        var res = "#############\n#"
        res += hallway.map { it ?: "." }.joinToString("")
        res += "#\n"
        for (i in rooms[0].indices) {
            res += "###"
            for (e in rooms) {
                res += e[i] ?: '.'
                res += "#"
            }
            res += "##\n"
        }
        res += "  #########  \n"
        return res
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GamePosition

        if (!rooms.contentDeepEquals(other.rooms)) return false
        if (!hallway.contentEquals(other.hallway)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rooms.contentDeepHashCode()
        result = 31 * result + hallway.contentHashCode()
        return result
    }

    companion object {
        val roomHallwayPos: Array<Int>
            get() = arrayOf(2, 4, 6, 8)
        val weights = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
        val roomMapping = mapOf('A' to 0, 'B' to 1, 'C' to 2, 'D' to 3)
        val reversedRoomMapping = arrayOf('A', 'B', 'C', 'D')
    }
}

data class DistNode<T>(val node: T, val dist: Int, val pred: DistNode<T>?) : Comparable<DistNode<T>> {
    override fun compareTo(other: DistNode<T>) = this.dist - other.dist
}

fun main() {
    fun preprocess(input: List<String>, part: Int): GamePosition {
        return if (part == 1) {
            GamePosition(
                arrayOf(
                    arrayOf(input[2][3], input[3][3]),
                    arrayOf(input[2][5], input[3][5]),
                    arrayOf(input[2][7], input[3][7]),
                    arrayOf(input[2][9], input[3][9]),
                ), arrayOfNulls(11)
            )
        } else {
            GamePosition(
                arrayOf(
                    arrayOf(input[2][3], input[3][3], input[4][3], input[5][3]),
                    arrayOf(input[2][5], input[3][5], input[4][5], input[5][5]),
                    arrayOf(input[2][7], input[3][7], input[4][7], input[5][7]),
                    arrayOf(input[2][9], input[3][9], input[4][9], input[5][9]),
                ), arrayOfNulls(11)
            )
        }
    }

    fun findShortestPath(start: GamePosition, end: GamePosition): Int {
        val visited = mutableSetOf<GamePosition>()
        val queue = PriorityQueue<DistNode<GamePosition>>()
        queue.add(DistNode(start, 0, null))
        while (queue.isNotEmpty()) {
            val distNode = queue.poll()
            val (node, dist, _) = distNode
            if (visited.contains(node)) continue
            if (node == end) return dist
            visited.add(node)
            for ((next, cost) in node.successors()) {
                queue.add(DistNode(next, dist + cost, distNode))
            }
        }
        throw IllegalArgumentException("Not found")
    }

    fun part1(initialPos: GamePosition): Int {
        return findShortestPath(
            initialPos, GamePosition(
                arrayOf(
                    arrayOf('A', 'A'), arrayOf('B', 'B'), arrayOf('C', 'C'), arrayOf('D', 'D')
                ), arrayOfNulls(11)
            )
        )
    }

    fun part2(initialPos: GamePosition): Int {
        return findShortestPath(
            initialPos, GamePosition(
                arrayOf(
                    arrayOf('A', 'A', 'A', 'A'),
                    arrayOf('B', 'B', 'B', 'B'),
                    arrayOf('C', 'C', 'C', 'C'),
                    arrayOf('D', 'D', 'D', 'D')
                ), arrayOfNulls(11)
            )
        )
    }

    val testInput = readInput(23, true).toMutableList()
    val input = readInput(23).toMutableList()

    check(part1(preprocess(testInput, 1)) == 12521)
    println(part1(preprocess(input, 1)))

    testInput.add(3, "  #D#B#A#C#")
    testInput.add(3, "  #D#C#B#A#")
    input.add(3, "  #D#B#A#C#")
    input.add(3, "  #D#C#B#A#")

    check(part2(preprocess(testInput, 2)) == 44169)
    println(part2(preprocess(input, 2)))
}
