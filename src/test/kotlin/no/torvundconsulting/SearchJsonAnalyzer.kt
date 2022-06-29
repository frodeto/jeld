package no.torvundconsulting

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SearchJsonAnalyzer {

    @Test
    fun analyzeKundeSearch() {
        val c1 = readJsonFromCsvColumn("_raw")
        assertEquals(3, c1.size)

        val gson = Gson()

        for (s in c1) {
            val ce = gson.fromJson(s, CustomerEntity::class.java)
            assertNotNull(ce)
        }
    }

    companion object {
        fun readJsonFromCsvColumn(column_name: String) : List<String> {
            val search_entities = mutableListOf<String>()
            csvReader().open("src/test/resources/analyze_search_test.csv") {
                readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                    val raw = row[column_name]
                    val entityStartIndex = raw?.indexOf("Entity:")
                    val entityEndIndex = raw?.indexOf('}', entityStartIndex ?: 0, false)
                    if (entityStartIndex == null || entityEndIndex == null) {
                        throw Error("Could not locate entity")
                    }
                    val start: Int = entityStartIndex + 8
                    val end: Int = entityEndIndex + 1
                    val eString = raw.substring(start, end)
                    search_entities.add(eString)
                }
            }
            return search_entities
        }
    }
}