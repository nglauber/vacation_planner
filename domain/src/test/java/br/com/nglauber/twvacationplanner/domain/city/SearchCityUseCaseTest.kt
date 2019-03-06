package br.com.nglauber.twvacationplanner.domain.city

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.repository.Cache
import br.com.nglauber.twvacationplanner.data.repository.CityRepository
import br.com.nglauber.twvacationplanner.data.repository.Remote
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchCityUseCaseTest {

    private lateinit var useCase: SearchCityUseCase

    private lateinit var repository: CityRepository
    private lateinit var remote: Remote
    private lateinit var cache: Cache<City>

    private lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup() {
        postExecutionThread = mock()
        remote = mock()
        cache = mock()
        repository = CityRepository(remote, cache)
        useCase = SearchCityUseCase(repository, postExecutionThread)
    }

    @Test
    fun searchCitiesCompletes() {
        val list = DataMockFactory.makeCitiesList(3)
        stubMocks(list)
        useCase
            .buildUseCaseObservable("")
            .test()
            .assertComplete()
    }

    @Test
    fun getCitiesReturnsData() {
        val list = DataMockFactory.makeCitiesList(3)
        stubMocks(list)
        useCase.buildUseCaseObservable("")
            .test()
            .assertValue { result ->
                result.sortedBy { it.id } == list.sortedBy { it.id }
            }
    }

    @Test
    fun filterCitiesReturnsData() {
        val cityRecife = City("001", "Recife", "Pernambuco", "PE", "Brazil")
        val cityRio = City("002", "Rio de Janeiro", "Rio de Janeito", "RJ", "Brazil")
        val citySampa = City("003", "São Paulo", "São Paulo", "SP", "Brazil")
        val list = listOf(cityRecife, citySampa, cityRio)
        stubMocks(list)
        useCase.buildUseCaseObservable("R")
            .test()
            .assertValue { result ->
                result.contains(cityRecife) && result.contains(cityRio) && !result.contains(citySampa)
            }
    }

    private fun stubMocks(list: List<City>) {
        val observable = Observable.just(list)
        whenever(remote.getCities()).thenReturn(observable)
        whenever(cache.getFromCache()).thenReturn(observable)
        whenever(cache.saveToCache(any())).thenReturn(Completable.complete())
    }
}