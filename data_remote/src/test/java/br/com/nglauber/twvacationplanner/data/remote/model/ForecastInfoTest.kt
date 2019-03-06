package br.com.nglauber.twvacationplanner.data.remote.model

import br.com.nglauber.twvacationplanner.data.model.TemperatureInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ForecastInfoTest {

    @Test
    fun readProps() {
        val date = Date()
        val temperatureInfo = TemperatureInfo(35, 16, "C")
        val cityId = "001"
        val weatherType = "Clear"

        val forecastInfo = ForecastInfo(date, temperatureInfo, cityId, weatherType)
        assertNotNull(forecastInfo)
        assertNotNull(forecastInfo.date)
        assertEquals(forecastInfo.date, date)
        assertNotNull(forecastInfo.temperature)
        assertEquals(forecastInfo.temperature, temperatureInfo)
        assertEquals(forecastInfo.cityId, cityId)
        assertEquals(forecastInfo.weather, weatherType)
    }
}