package br.com.nglauber.twvacationplanner.domain.weathertype

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.repository.Cache
import br.com.nglauber.twvacationplanner.data.repository.Remote
import br.com.nglauber.twvacationplanner.data.repository.WeatherTypeRepository
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchWeatherTypeUseCaseTest {

    private lateinit var useCase: SearchWeatherTypeUseCase

    private lateinit var repository: WeatherTypeRepository
    private lateinit var remote: Remote
    private lateinit var cache: Cache<WeatherType>

    private lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup() {
        postExecutionThread = mock()
        remote = mock()
        cache = mock()
        repository = WeatherTypeRepository(remote, cache)
        useCase = SearchWeatherTypeUseCase(repository, postExecutionThread)
    }

    @Test
    fun searchWeatherTypesCompletes() {
        val list = DataMockFactory.makeWeatherTypeList(3)
        stubMocks(list)
        useCase
            .buildUseCaseObservable("")
            .test()
            .assertComplete()
    }

    @Test
    fun getWeatherTypesReturnsData() {
        val list = DataMockFactory.makeWeatherTypeList(3)
        stubMocks(list)
        useCase.buildUseCaseObservable("")
            .test()
            .assertValue { result ->
                result.sortedBy { it.id } == list.sortedBy { it.id }
            }
    }

    @Test
    fun filterWeatherTypesReturnsData() {
        val wtCloudy = WeatherType("001", "Cloudy")
        val wtClear = WeatherType("002", "Clear")
        val wtSnow = WeatherType("003", "Snow")
        val list = listOf(wtCloudy, wtClear, wtSnow)
        stubMocks(list)
        useCase.buildUseCaseObservable("Cl")
            .test()
            .assertValue { result ->
                result.contains(wtCloudy) && result.contains(wtClear) && !result.contains(wtSnow)
            }
    }

    private fun stubMocks(list: List<WeatherType>) {
        val observable = Observable.just(list)
        whenever(remote.getWeatherTypes()).thenReturn(observable)
        whenever(cache.getFromCache()).thenReturn(observable)
        whenever(cache.saveToCache(any())).thenReturn(Completable.complete())
    }
}