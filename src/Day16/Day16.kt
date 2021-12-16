import java.lang.IllegalArgumentException

fun main() {
    val hexToBin = mapOf(
        "0" to "0000",
        "1" to "0001",
        "2" to "0010",
        "3" to "0011",
        "4" to "0100",
        "5" to "0101",
        "6" to "0110",
        "7" to "0111",
        "8" to "1000",
        "9" to "1001",
        "A" to "1010",
        "B" to "1011",
        "C" to "1100",
        "D" to "1101",
        "E" to "1110",
        "F" to "1111"
    )

    // (version sum, length, value)
    fun encode(packet: String): Triple<Int, Int, Long> {
        val version = Integer.parseInt(packet.slice(0 until 3), 2)
        val typeId = Integer.parseInt(packet.slice(3 until 6), 2)
        if (typeId == 4) {
            var value = ""
            var nextStart = 6
            do {
                val nextFour = packet.slice(nextStart until nextStart + 5)
                value += nextFour.slice(1 until 5)
                nextStart += 5
            } while (nextFour[0] == '1')
            return Triple(version, nextStart, value.toLong(2))
        }
        val mode = packet[6].toString().toInt()
        val subPackets = mutableListOf<Triple<Int, Int, Long>>()
        var offset = 0
        if (mode == 0) {
            val length = Integer.parseInt(packet.slice(7 until 22), 2)
            var nextStart = 22
            var summedLength = 0
            while (summedLength != length) {
                val subPacket = packet.slice(nextStart until packet.length)
                val packetRes = encode(subPacket)
                subPackets.add(packetRes)
                summedLength += packetRes.second
                nextStart += packetRes.second
            }
            offset = 22
        } else if (mode == 1) {
            val num = Integer.parseInt(packet.slice(7 until 18), 2)
            var nextStart = 18
            for (i in 0 until num) {
                val subPacket = packet.slice(nextStart until packet.length)
                val packetRes = encode(subPacket)
                subPackets.add(packetRes)
                nextStart += packetRes.second
            }
            offset = 18
        }
        val res = when (typeId) {
            0 -> subPackets.sumOf { it.third }
            1 -> subPackets.fold(1L) { acc, i -> acc * i.third }
            2 -> subPackets.minOf { it.third }
            3 -> subPackets.maxOf { it.third }
            5 -> if (subPackets[0].third > subPackets[1].third) 1 else 0
            6 -> if (subPackets[0].third < subPackets[1].third) 1 else 0
            7 -> if (subPackets[0].third == subPackets[1].third) 1 else 0
            else -> throw IllegalArgumentException()

        }
        return Triple(subPackets.sumOf { it.first } + version, subPackets.sumOf { it.second } + offset, res)
    }

    fun part1(input: String): Int {
        val bin = input.map { hexToBin.getOrDefault(it.toString(), "X") }.joinToString("") { it }
        val packet = encode(bin)
        return packet.first
    }

    fun part2(input: String): Long {
        val bin = input.map { hexToBin.getOrDefault(it.toString(), "X") }.joinToString("") { it }
        val packet = encode(bin)
        return packet.third
    }

    val testInput = readInput(16, true)[0]
    val input = readInput(16)[0]

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 54L)
    println(part2(input))
}
