package br.com.nglauber.twvacationplanner.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class VacationSuggestionTest {

    @Test
    fun readProps() {
        val startDate = Date()
        val endDate = Date()
        val forecastInfo = mutableListOf<ForecastInfo>()

        val vacationSuggestion = VacationSuggestion(startDate, endDate, forecastInfo)
        assertNotNull(vacationSuggestion)
        assertEquals(vacationSuggestion.startDate, startDate)
        assertEquals(vacationSuggestion.endDate, endDate)
        assertEquals(vacationSuggestion.forecastInfo, forecastInfo)
    }
}