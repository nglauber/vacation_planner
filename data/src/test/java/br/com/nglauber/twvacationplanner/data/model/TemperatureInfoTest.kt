package br.com.nglauber.twvacationplanner.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TemperatureInfoTest {

    @Test
    fun readProps() {
        val max = 35
        val min = 16
        val unit = "C"

        val temperatureInfo = TemperatureInfo(max, min, unit)
        assertNotNull(temperatureInfo)
        assertEquals(temperatureInfo.max, max)
        assertEquals(temperatureInfo.min, min)
        assertEquals(temperatureInfo.unit, unit)
    }
}