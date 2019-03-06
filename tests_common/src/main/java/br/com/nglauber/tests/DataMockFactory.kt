package br.com.nglauber.tests

import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.TemperatureInfo
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import java.util.*

object DataMockFactory {
    fun makeCitiesList(count: Int) : List<City> {
        return (1..count).map { i ->
            makeCity(i)
        }
    }

    fun makeCity(i: Int = randomInt()) =
        City(
            i.toString(),
            randomString(),
            randomString(),
            randomString(),
            randomString()
        )

    fun makeWeatherTypeList(count: Int) : List<WeatherType> {
        return (1..count).map { i ->
            makeWeatherType(i)
        }
    }

    fun makeWeatherType(i: Int = randomInt()) =
        WeatherType(
            i.toString(),
            randomString()
        )

    fun makeTemperatureInfo() : TemperatureInfo =
        TemperatureInfo(randomInt(50), randomInt(50), randomString())

    fun makeForecastInfoList(count: Int) : List<ForecastInfo> {
        val cityId = randomString()
        return (1..count).map {
            ForecastInfo(Date(), makeTemperatureInfo(), cityId, randomString())
        }
    }

    private fun randomString() = UUID.randomUUID().toString()

    private fun randomInt(max: Int = Integer.MAX_VALUE) = Random().nextInt(max) + 1
}