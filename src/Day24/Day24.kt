fun main() {
    fun preprocess(input: List<String>): List<List<String>> {
        return input.map { it.split(" ") }
    }

    fun split(input: List<List<String>>): MutableList<List<List<String>>> {
        val blocks = mutableListOf<List<List<String>>>()
        var block = mutableListOf<List<String>>()
        for (e in input) {
            if (e[0] == "inp") {
                if (block.isNotEmpty()) {
                    blocks.add(block)
                    block = mutableListOf()
                }
            }
            block.add(e)
        }
        blocks.add(block)
        return blocks
    }

    fun doBackwardStep(block: List<List<String>>, w: Int, z: Int): List<Int> {
        val divZ = block[4][2].toInt() // C
        val addX = block[5][2].toInt() // A
        val addY = block[15][2].toInt() // B

        val res = mutableListOf<Int>()
        val y0 = z - w - addY
        if (y0 % 26 == 0) {
            res.add(y0 / 26 * divZ)
        }
        if (w - addX in 0..25) {
            val y1 = z * divZ
            res.add(w - addX + y1)
        }

        return res
    }

    fun calc(blocks: List<List<List<String>>>, order: IntProgression): Long {
        var res = mutableMapOf(0 to "")
        for (block in blocks.reversed()) {
            val resNew = mutableMapOf<Int, String>()
            for ((z, inputStr) in res) for (w in order) {
                val newZ = doBackwardStep(block, w, z)
                resNew.putAll(newZ.map { it to w.toString() + inputStr })
            }
            res = resNew
        }
        return res[0]?.toLong() ?: throw IllegalStateException()
    }

    fun part1(input: List<List<String>>): Long {
        val blocks = split(input)
        return calc(blocks, 1..9)
    }

    fun part2(input: List<List<String>>): Long {
        val blocks = split(input)
        return calc(blocks, 9 downTo 1)
    }

    val input = preprocess(readInput(24))

    println(part1(input))
    println(part2(input))
}
