@file:JvmName("LogParsers")
package no.torvundconsulting

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader


fun readJsonFromCsvColumn(file_path: String, column_name: String, jsonTag: String) : List<String> {
    val search_entities = mutableListOf<String>()
    csvReader().open(file_path) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            val raw = row[column_name]
            val entityStartIndex = raw?.indexOf(jsonTag)
            val entityEndIndex = raw?.indexOf('}', entityStartIndex ?: 0, false)
            if (entityStartIndex == null || entityEndIndex == null) {
                throw Error("Could not locate entity")
            }
            val start: Int = entityStartIndex + jsonTag.length
            val end: Int = entityEndIndex + 1
            val eString = try {
                raw.substring(start, end)
            } catch (exception: StringIndexOutOfBoundsException) {
                println("Failed to parse line")
                "{}"
            }

            search_entities.add(eString)
        }
    }
    return search_entities
}