fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input[0].split(' ').filter { it != "seeds:" }.map { it.toLong() }
        val seedSoil = parseMapping(input.indexOf("seed-to-soil map:") + 1, input)
        val soidFert = parseMapping(input.indexOf("soil-to-fertilizer map:") + 1, input)
        val fertWater = parseMapping(input.indexOf("fertilizer-to-water map:") + 1, input)
        val waterLight = parseMapping(input.indexOf("water-to-light map:") + 1, input)
        val lightTemp = parseMapping(input.indexOf("light-to-temperature map:") + 1, input)
        val tempHum = parseMapping(input.indexOf("temperature-to-humidity map:") + 1, input)
        val humLoc = parseMapping(input.indexOf("humidity-to-location map:") + 1, input)

        return seeds
            .asSequence()
            .map { findMapping(it, seedSoil)}
            .map { findMapping(it, soidFert) }
            .map { findMapping(it, fertWater) }
            .map { findMapping(it, waterLight) }
            .map { findMapping(it, lightTemp) }
            .map { findMapping(it, tempHum) }
            .map { findMapping(it, humLoc) }
            .toList()
            .min()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input[0]
            .split(' ')
            .filter { it != "seeds:" }
            .map { it.toLong() }
            .chunked(2)
            .map { LongRange(it[0], it[0]+it[1]-1) }

        val seedSoil = parseMapping(input.indexOf("seed-to-soil map:") + 1, input)
        val soidFert = parseMapping(input.indexOf("soil-to-fertilizer map:") + 1, input)
        val fertWater = parseMapping(input.indexOf("fertilizer-to-water map:") + 1, input)
        val waterLight = parseMapping(input.indexOf("water-to-light map:") + 1, input)
        val lightTemp = parseMapping(input.indexOf("light-to-temperature map:") + 1, input)
        val tempHum = parseMapping(input.indexOf("temperature-to-humidity map:") + 1, input)
        val humLoc = parseMapping(input.indexOf("humidity-to-location map:") + 1, input)


        var currentLocation = 0L
        while (true) {
            if (seedRanges.isInAnyRange(currentLocation
                .let { findMappingReverse(it, humLoc) }
                .let { findMappingReverse(it, tempHum) }
                .let { findMappingReverse(it, lightTemp) }
                .let { findMappingReverse(it, waterLight) }
                .let { findMappingReverse(it, fertWater) }
                .let { findMappingReverse(it, soidFert) }
                .let { findMappingReverse(it, seedSoil)})
                ) {
                return currentLocation
            }
            currentLocation++
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("05")
    part1(input).println()
    part2(input).println()
}

private fun findMapping(from: Long, mappings: List<MappingRange>): Long {
    return mappings.find { it.from.contains(from) }?.let { from + it.offset }?:from
}
private fun findMappingReverse(from: Long, mappings: List<MappingRange>): Long {
    return mappings.find { it.to.contains(from) }?.let { from - it.offset }?:from
}

private fun List<LongRange>.isInAnyRange(x: Long): Boolean =
    this.none { it.contains(x) }.not()

private fun parseMapping(startIndex: Int, input: List<String>): List<MappingRange> {
    val res = mutableListOf<MappingRange>()
    var idx = startIndex
    while(idx < input.size){
        if (input[idx] == ""){
            return res
        }
        input[idx].split(' ').map { it.toLong() }.also {
            res.add(MappingRange(
                LongRange(it[1], it[1] + it[2]-1),
                LongRange(it[0], it[0] + it[2]-1),
                it[0] - it[1]
            ))
        }
        idx++
    }
    return res
}

data class MappingRange(
    val from: LongRange,
    val to: LongRange,
    val offset: Long
)