package br.com.nglauber.twvacationplanner.local

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CitiesMemoryCacheTest {

    private lateinit var cache : MemoryCache<City>

    @Before
    fun init() {
        cache = MemoryCache()
    }

    @Test
    fun assertSaveCitiesToCacheCompletes() {
        cache.saveToCache(*DataMockFactory.makeCitiesList(2).toTypedArray())
            .test()
            .assertComplete()
    }

    @Test
    fun assertGetCitiesFromCacheCompletes() {
        val listSize = 2
        val list = DataMockFactory.makeCitiesList(listSize)
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
    fun assertGetCitiesFromCacheGetData() {
        val listSize = 2
        val list = DataMockFactory.makeCitiesList(listSize)
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