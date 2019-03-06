package br.com.nglauber.twvacationplanner.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherTypeTest {

    @Test
    fun readProps() {
        val id = "001"
        val name = "Cloudy"

        val weatherType = WeatherType(id, name)
        assertNotNull(weatherType)
        assertEquals(weatherType.id, id)
        assertEquals(weatherType.name, name)
    }
}