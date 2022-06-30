package no.torvundconsulting

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SearchJsonAnalyzer {

    @Test
    fun analyzeKundeSearch() {
        val c1 = readJsonFromCsvColumn("src/test/resources/analyze_search_test.csv", "_raw", "Entity:")
        assertEquals(3, c1.size)

        val gson = Gson()

        for (s in c1) {
            val ce = gson.fromJson(s, CustomerEntity::class.java)
            assertNotNull(ce)
        }
    }

    companion object {

    }
}