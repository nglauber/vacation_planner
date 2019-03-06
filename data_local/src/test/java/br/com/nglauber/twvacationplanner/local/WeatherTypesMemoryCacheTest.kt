package br.com.nglauber.twvacationplanner.local

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherTypesMemoryCacheTest {

    private lateinit var cache : MemoryCache<WeatherType>

    @Before
    fun init() {
        cache = MemoryCache()
    }

    @Test
    fun assertSaveWeatherTypesToCacheCompletes() {
        cache.saveToCache(*DataMockFactory.makeWeatherTypeList(2).toTypedArray())
            .test()
            .assertComplete()
    }

    @Test
    fun assertGetWeatherTypesFromCacheCompletes() {
        val listSize = 2
        val list = DataMockFactory.makeWeatherTypeList(listSize)
        cache.saveToCache(*list.toTypedArray())
            .test()
            .assertComplete()
        cache.getFromCache()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { result ->
                result.size == listSize
            }
    }

    @Test
    fun assertGetWeatherTypesFromCacheGetData() {
        val listSize = 2
        val list = DataMockFactory.makeWeatherTypeList(listSize)
        cache.saveToCache(*list.toTypedArray())
            .test()
            .assertComplete()
        cache.getFromCache()
            .test()
            .assertValue{ result ->
                result.size == listSize &&
                result.sortedBy { it.id } == list.sortedBy { it.id }
            }
    }
}