fun main() {
    fun part1(input: List<String>): Int {
        return input.map {line ->
            ((line.findFirstDigit() ?: "") + (line.reversed().findFirstDigit() ?: "")).toInt()
        }.reduce { acc, next -> acc + next }
    }

    fun part2(input: List<String>): Int {
        return input
                .map {
                    it.replaceWords()}
                .map {line ->
                    ((line.findFirstDigit() ?: "") + (line.reversed().findFirstDigit() ?: "")).toInt() }
                .reduce { acc, next -> acc + next }
    }

    // test if implementation meets criteria from the description, like:
     val testInput = readInput("01_test")
     check(part2(testInput) == 281)

    val input = readInput("01")
    part1(input).println()
    part2(input).println()
}

private fun String.findFirstDigit(): String? {
    return this.find { it.isDigit() }?.toString()
}

private fun String.replaceWords(): String {
    val stringBuilder = StringBuilder(this)
    stringBuilder.findAnyOf(DIGIT_WORDS)?.let {
        stringBuilder.insert(it.first, it.second.wordsToDigits())
    }
    stringBuilder.findLastAnyOf(DIGIT_WORDS)?.let {
        stringBuilder.insert(it.first, it.second.wordsToDigits())
    }
    return stringBuilder.toString()
}

private fun String.wordsToDigits(): String {
    return this
            .replace("one", "1")
            .replace("two", "2")
            .replace("three", "3")
            .replace("four", "4")
            .replace("five", "5")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8")
            .replace("nine", "9")
}

val DIGIT_WORDS = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")