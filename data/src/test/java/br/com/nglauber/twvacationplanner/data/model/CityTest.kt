package br.com.nglauber.twvacationplanner.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityTest {

    @Test
    fun readProps() {
        val id = "001"
        val district = "Recife"
        val province = "Pernambuco"
        val state = "PE"
        val country = "Brazil"

        val city = City(id, district, province, state, country)
        assertNotNull(city)
        assertEquals(city.id, id)
        assertEquals(city.district, district)
        assertEquals(city.province, province)
        assertEquals(city.state, state)
        assertEquals(city.country, country)
    }
}