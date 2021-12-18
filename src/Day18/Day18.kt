fun main() {
    fun preprocess(input: List<String>): List<MutableList<String>> {
        fun mapFun(s: String): List<String> =
            s.replace("]", "#]#").replace("[", "#[#").replace(",", "#,#").split("#").filter { it != "" }
        return input.map { mapFun(it).toMutableList() }
    }

    fun reduce(input: MutableList<String>): MutableList<String> {
        while (true) {
            var pos = -1
            var depth = 0
            var split = false
            for ((i, e) in input.withIndex()) {
                if (e == "[") {
                    depth++
                } else if (e == "]") {
                    depth--
                } else if (e != ",") {
                    if (e.toInt() > 9) {
                        split = false
                        pos = i
                        break
                    }
                }
            }
            depth = 0
            for ((i, e) in input.withIndex()) {
                if (e == "[") {
                    depth++
                } else if (e == "]") {
                    depth--
                } else if (e == ",") {
                    if (input[i + 1] != "[" && input[i - 1] != "]") {
                        if (depth > 4) {
                            split = true
                            pos = i
                            break
                        }
                    }
                }
            }
            if (pos == -1) {
                break
            }
            if (split) {
                val before = input[pos - 1].toInt()
                val after = input[pos + 1].toInt()
                for (i in pos - 2 downTo 0) {
                    if (input[i] != "," && input[i] != "[" && input[i] != "]") {
                        input[i] = (input[i].toInt() + before).toString()
                        break
                    }
                }
                for (i in pos + 2 until input.size) {
                    if (input[i] != "," && input[i] != "[" && input[i] != "]") {
                        input[i] = (input[i].toInt() + after).toString()
                        break
                    }
                }
                for (i in 1..5) {
                    input.removeAt(pos - 2)
                }
                input.add(pos - 2, "0")
            } else {
                val num = input[pos].toInt()
                input.removeAt(pos)
                input.addAll(pos, listOf("[", (num / 2).toString(), ",", (num / 2 + num % 2).toString(), "]"))
            }
        }
        return input
    }

    fun add(in1: List<String>, in2: List<String>): MutableList<String> {
        val res = mutableListOf("[")
        res.addAll(in1)
        res.add(",")
        res.addAll(in2)
        res.add("]")
        return res
    }

    fun magnitude(input: MutableList<String>): Int {
        while (input.size > 1) {
            var toReduce = -1
            for ((i, e) in input.withIndex()) {
                if (e == ",") {
                    if (input[i + 1] != "[" && input[i - 1] != "]") {
                        toReduce = i
                        break
                    }
                }
            }
            val fst = input[toReduce - 1].toInt()
            val snd = input[toReduce + 1].toInt()
            for (i in 1..5) {
                input.removeAt(toReduce - 2)
            }
            input.add(toReduce - 2, (fst * 3 + snd * 2).toString())
        }
        return input[0].toInt()
    }

    fun part1(input: List<MutableList<String>>): Int {
        var res = input[0]
        for (e in input.drop(1)) {
            res = add(res, e)
            res = reduce(res)
        }
        return magnitude(res)
    }

    fun part2(input: List<MutableList<String>>): Int {
        var maxVal = -1
        for (e1 in input) {
            for (e2 in input) {
                val res = reduce(add(e1, e2))
                val mag = magnitude(res)
                if (mag > maxVal) {
                    maxVal = mag
                }
            }
        }
        return maxVal
    }

    val testInput = preprocess(readInput(18, true))
    val input = preprocess(readInput(18))

    check(part1(testInput) == 4140)
    println(part1(input))

    check(part2(testInput) == 3993)
    println(part2(input))
}
