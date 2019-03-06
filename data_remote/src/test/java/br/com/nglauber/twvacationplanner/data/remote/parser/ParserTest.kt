package br.com.nglauber.twvacationplanner.data.remote.parser

import br.com.nglauber.twvacationplanner.data.model.TemperatureInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import br.com.nglauber.twvacationplanner.data.remote.model.City
import br.com.nglauber.twvacationplanner.data.remote.model.ForecastInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import java.util.*
import br.com.nglauber.twvacationplanner.data.model.City as CityData
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo as ForecastInfoData

@RunWith(JUnit4::class)
class ParserTest {

    @Test
    fun cityToCityDataTest() {
        val id = "001"
        val district = "Recife"
        val province = "Pernambuco"
        val state = "PE"
        val country = "Brazil"

        val city = City(id, district, province, state, country)
        val cityData = Parser.toData(city)
        assertNotNull(cityData)
        assertEquals(city.id, cityData.id)
        assertEquals(city.district, cityData.district)
        assertEquals(city.province, cityData.province)
        assertEquals(city.state, cityData.state)
        assertEquals(city.country, cityData.country)
    }

    @Test
    fun forecastInfoToForecastInfoData() {
        val date = Date()
        val temperatureInfo = TemperatureInfo(35, 16, "C")
        val cityId = "001"
        val weatherType = "Clear"

        val forecastInfo = ForecastInfo(date, temperatureInfo, cityId, weatherType)
        val forecastInfoData = Parser.toData(forecastInfo)
        assertNotNull(forecastInfoData)
        assertEquals(forecastInfo.cityId, forecastInfoData.cityId)
        assertEquals(forecastInfo.weather, forecastInfoData.weather)
        assertEquals(forecastInfo.date, forecastInfoData.date)
        assertEquals(forecastInfo.temperature, forecastInfoData.temperature)
    }
}