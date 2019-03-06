package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherTypeRepositoryTest {
    private lateinit var remote: Remote
    private lateinit var cache: Cache<WeatherType>
    private lateinit var repository: WeatherTypeRepository

    @Before
    fun setup() {
        remote = mock()
        cache = mock()
        repository = WeatherTypeRepository(remote, cache)
    }

    @Test
    fun getWeatherTypesCompletes() {
        val list = DataMockFactory.makeWeatherTypeList(2)
        stubRemote(Observable.just(list))
        stubSaveToCache()
        stubGetFromCache(Observable.just(list))
        repository.searchWeatherTypes("")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getWeatherTypesCallsCache() {
        val listSize = 2
        val listRemote = DataMockFactory.makeWeatherTypeList(listSize)
        val listLocal = DataMockFactory.makeWeatherTypeList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.just(listLocal))
        repository.searchWeatherTypes("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { result ->
                assertEquals(listSize, result.size)
                result.sortedBy { it.id } == listLocal.sortedBy { it.id }
            }
    }

    @Test
    fun getWeatherTypesCallsServer() {
        val listSize = 2
        val listRemote = DataMockFactory.makeWeatherTypeList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.empty())
        repository.searchWeatherTypes("")
            .doOnError { e -> e.printStackTrace() }
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { result ->
                result.size == listSize && result.sortedBy { it.id } == listRemote.sortedBy { it.id }
            }
    }

    @Test
    fun getFilteredWeatherTypes() {
        val wtCloudy = WeatherType("001", "Cloudy")
        val wtClear = WeatherType("002", "Clear")
        val wtSnow = WeatherType("003", "Snow")
        val listRemote = listOf(wtCloudy, wtClear, wtSnow)

        stubGetFromCache(Observable.empty())
        stubSaveToCache()
        stubRemote(Observable.just(listRemote))

        repository.searchWeatherTypes("Cl")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { list ->
                list.contains(wtCloudy) && list.contains(wtClear) && !list.contains(wtSnow)
            }
    }

    @Test
    fun getNotExistingWeatherTypeReturnEmptyList() {
        val listSize = 2
        val listRemote = DataMockFactory.makeWeatherTypeList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.empty())

        repository.searchWeatherTypes("A non existing weather type")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { list ->
                list.isEmpty()
            }
    }

    private fun stubRemote(observable: Observable<List<WeatherType>>) {
        whenever(remote.getWeatherTypes()).thenReturn(observable)
    }

    private fun stubGetFromCache(observable: Observable<List<WeatherType>>) {
        whenever(cache.getFromCache()).thenReturn(observable)
    }

    private fun stubSaveToCache() {
        whenever(cache.saveToCache(any())).thenReturn(Completable.complete())
    }
}