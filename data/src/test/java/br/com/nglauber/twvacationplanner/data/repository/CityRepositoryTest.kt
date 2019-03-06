package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
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
class CityRepositoryTest {
    private lateinit var remote: Remote
    private lateinit var cache: Cache<City>
    private lateinit var repository: CityRepository

    @Before
    fun setup() {
        remote = mock()
        cache = mock()
        repository = CityRepository(remote, cache)
    }

    @Test
    fun getCitiesCompletes() {
        val list = DataMockFactory.makeCitiesList(2)
        stubRemote(Observable.just(list))
        stubSaveToCache()
        stubGetFromCache(Observable.just(list))
        repository.searchCities("")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getCitiesCallsCache() {
        val listSize = 2
        val listRemote = DataMockFactory.makeCitiesList(listSize)
        val listLocal = DataMockFactory.makeCitiesList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.just(listLocal))
        repository.searchCities("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { result ->
                assertEquals(listSize, result.size)
                assert(result.sortedBy { it.id } == listLocal.sortedBy { it.id })
                true
            }
    }

    @Test
    fun getCitiesCallsServer() {
        val listSize = 2
        val listRemote = DataMockFactory.makeCitiesList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.empty())
        repository.searchCities("")
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
    fun getFilteredCities() {
        val cityRecife = City("001", "Recife", "", "", "")
        val citySampa = City("002", "São Paulo", "", "", "")
        val cityRio = City("003", "Rio de Janeiro", "", "", "")
        val listRemote = listOf(cityRecife, citySampa, cityRio)

        stubGetFromCache(Observable.empty())
        stubSaveToCache()
        stubRemote(Observable.just(listRemote))

        repository.searchCities("R")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { list ->
                list.contains(cityRecife) && list.contains(cityRio) && !list.contains(citySampa)
            }
    }

    @Test
    fun getNotExistingCityReturnEmptyList() {
        val listSize = 2
        val listRemote = DataMockFactory.makeCitiesList(listSize)
        stubRemote(Observable.just(listRemote))
        stubSaveToCache()
        stubGetFromCache(Observable.empty())

        repository.searchCities("A non existing city")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { list ->
                list.isEmpty()
            }
    }

    private fun stubRemote(observable: Observable<List<City>>) {
        whenever(remote.getCities()).thenReturn(observable)
    }

    private fun stubGetFromCache(observable: Observable<List<City>>) {
        whenever(cache.getFromCache()).thenReturn(observable)
    }

    private fun stubSaveToCache() {
        whenever(cache.saveToCache(any())).thenReturn(Completable.complete())
    }
}