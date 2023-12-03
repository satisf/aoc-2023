import java.util.ArrayList
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val inputMap = input.toTypedArray().map { it.toCharArray() }
        return inputMap
            .mapIndexed { lineIdx, line -> findPartNumbersInLine(line, lineIdx, inputMap) }
            .flatten()
            .sumUp()
    }

    fun part2(input: List<String>): Int {
        val inputMap = input.toTypedArray().map { it.toCharArray() }
        val gears = mutableMapOf<String, MutableList<Int>>()
        inputMap.forEachIndexed { yIndex, line ->
            line.forEachIndexed { xIndex, symbol ->
                if (symbol == '*') gears.put("${xIndex}:${yIndex}", mutableListOf())
            }
        }
        gears.println()
        inputMap.mapIndexed { lineIdx, line -> findGearPartNumbersInLine(line, lineIdx, inputMap, gears) }
        gears.println()
        return gears
            .filter { entry -> entry.value.size == 2 }
            .values
            .map { it
                .reduce {a,b -> a*b}
            }
            .sumUp()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("03")
    part1(input).println()
    part2(input).println()
}

data class Point(
    var x: Int,
    var y: Int
)

fun areaContainsSymbol(upperLeft: Point, lowerRight: Point, field: List<CharArray>): Boolean {
    upperLeft.moveToField(field)
    lowerRight.moveToField(field)
    return field
        .slice(upperLeft.y..lowerRight.y)
        .map { it.sliceArray(IntRange(upperLeft.x, lowerRight.x)) }
        .filter { line -> line.any { SYMBOLS.contains(it) } }
        .filterNot { it.isEmpty() }
        .isNotEmpty()
}
fun findGearInArea(upperLeft: Point, lowerRight: Point, field: List<CharArray>): Point? {
    upperLeft.moveToField(field)
    lowerRight.moveToField(field)
    for (y in upperLeft.y..lowerRight.y){
        for(x in upperLeft.x..lowerRight.x){
            if (field[y][x] == '*') return Point(x,y)
        }
    }
    return null
}

fun findPartNumbersInLine(line: CharArray, lineIndex: Int, field: List<CharArray>): List<Int> {
    var firstNumberIndex = -1
    var currentNumber = ArrayList<Char>()
    var partNumbers = ArrayList<Int>()
    for ((idx, currentSymbol)in line.withIndex()) {
        if (currentSymbol.isDigit()) {
            if (firstNumberIndex < 0) {
                firstNumberIndex = idx
            }
            currentNumber.add(currentSymbol)
        } else {
            if (firstNumberIndex != -1) {
                if (areaContainsSymbol(Point(firstNumberIndex - 1, lineIndex -1), Point(idx, lineIndex + 1), field)) {
                    partNumbers.add(currentNumber.toCharArray().concatToString().toInt())
                }
                firstNumberIndex = -1
                currentNumber = ArrayList<Char>()
            }
        }
    }
    if (firstNumberIndex != -1) {
        if (areaContainsSymbol(Point(firstNumberIndex - 1, lineIndex - 1), Point(line.size, lineIndex + 1), field)) {
            partNumbers.add(currentNumber.toCharArray().concatToString().toInt())
        }
    }
    return partNumbers
}

fun findGearPartNumbersInLine(line: CharArray, lineIndex: Int, field: List<CharArray>, gears: MutableMap<String, MutableList<Int>>): MutableMap<String, MutableList<Int>> {
    var firstNumberIndex = -1
    var currentNumber = ArrayList<Char>()
    for ((idx, currentSymbol)in line.withIndex()) {
        if (currentSymbol.isDigit()) {
            if (firstNumberIndex < 0) {
                firstNumberIndex = idx
            }
            currentNumber.add(currentSymbol)
        } else {
            if (firstNumberIndex != -1) {
                findGearInArea(Point(firstNumberIndex - 1, lineIndex -1), Point(idx, lineIndex + 1), field)?.also {
                    gears["${it.x}:${it.y}"]?.add(currentNumber.toCharArray().concatToString().toInt())
                }

                firstNumberIndex = -1
                currentNumber = ArrayList<Char>()
            }
        }
    }
    if (firstNumberIndex != -1) {
        findGearInArea(Point(firstNumberIndex - 1, lineIndex -1), Point(line.size, lineIndex + 1), field)?.also {
            gears["${it.x}:${it.y}"]?.add(currentNumber.toCharArray().concatToString().toInt())
        }
    }
    return gears
}





fun Point.moveToField(field: List<CharArray>) {
    if (this.x < 0) {
        this.x = 0
    }
    if (this.x >= field[0].size) {
        this.x =field[0].size - 1
    }
    if (this.y < 0) {
        this.y = 0
    }
    if (this.y >= field.size) {
        this.y = field.size - 1
    }
}

fun Point.isAdjacentTo(point: Point): Boolean = abs(this.x - point.x) <= 1 && abs(this.y - point.y) <= 1


val SYMBOLS = listOf('&', '*', '#', '%', '$', '-', '@', '=', '+', '/')