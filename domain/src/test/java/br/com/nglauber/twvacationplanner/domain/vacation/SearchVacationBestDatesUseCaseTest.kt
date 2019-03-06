package br.com.nglauber.twvacationplanner.domain.vacation

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.repository.ForecastInfoRepository
import br.com.nglauber.twvacationplanner.data.repository.Remote
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class SearchVacationBestDatesUseCaseTest {

    private lateinit var useCase: SearchVacationBestDatesUseCase

    private lateinit var repository: ForecastInfoRepository
    private lateinit var remote: Remote

    private lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup() {
        postExecutionThread = mock()
        remote = mock()
        repository = ForecastInfoRepository(remote)
        useCase = SearchVacationBestDatesUseCase(repository, postExecutionThread)
    }

    @Test
    fun searchVacationBestDatesCompletes() {
        val list = DataMockFactory.makeForecastInfoList(3)
        stubMocks(list)
        useCase.buildUseCaseObservable(
            SearchVacationBestDatesUseCase.Params("", 0, emptyList(), 15)
        )
            .test()
            .assertComplete()
    }

    @Test
    fun searchVacationBestDatesReturnsData() {
        val list = DataMockFactory.makeForecastInfoList(3)
        stubMocks(list)
        useCase.buildUseCaseObservable(
            SearchVacationBestDatesUseCase.Params("", 0, emptyList(), 15)
        )
            .test()
            .assertComplete()
    }

    @Test
    fun searchVacationBestDatesReturnsCorrectData() {
        val cityId = "001"
        val weatherList = listOf(
            "Sunny",
            "Cloud",
            "Foo",
            "Sunny", // From here
            "Cloud", // ..
            "Rain",  // ..
            "Sunny", // ..
            "Cloud", // ..
            "Rain",  // To here
            "Foo",
            "Cloud"
        )
        val forecastInfoList = weatherList.map { weather ->
            ForecastInfo(Date(), DataMockFactory.makeTemperatureInfo(), cityId, weather)
        }
        stubMocks(forecastInfoList)
        val params = SearchVacationBestDatesUseCase.Params(
            cityId = cityId,
            year = 2019,
            weatherTypes = listOf(
                WeatherType("001", "Sunny"),
                WeatherType("002", "Cloud"),
                WeatherType("003", "Rain")
            ),
            numberOfVacationDays = 3
        )
        useCase.buildUseCaseObservable(params)
            .test()
            .assertComplete()
            .assertValueCount(1)
            .assertValue { result ->
                assert(result.size == 1)
                val first = result.first()
                first.forecastInfo?.size == 6
            }
    }

    @Test
    fun searchVacationBestDatesReturnsCorrectMoreThanOneSuggestion() {
        val cityId = "001"
        val weatherList = listOf(
            "Sunny",
            "Cloud",
            "Foo",
            "Sunny", // From here
            "Cloud", // ..
            "Rain",  // ..
            "Sunny", // ..
            "Cloud", // ..
            "Rain",  // To here
            "Foo",
            "Cloud", // And from here
            "Sunny", // ..
            "Sunny"  // To Here
        )
        val forecastInfoList = weatherList.map { weather ->
            ForecastInfo(Date(), DataMockFactory.makeTemperatureInfo(), cityId, weather)
        }
        stubMocks(forecastInfoList)
        val params = SearchVacationBestDatesUseCase.Params(
            cityId = cityId,
            year = 2019,
            weatherTypes = listOf(
                WeatherType("001", "Sunny"),
                WeatherType("002", "Cloud"),
                WeatherType("003", "Rain")
            ),
            numberOfVacationDays = 3
        )
        useCase.buildUseCaseObservable(params)
            .test()
            .assertComplete()
            .assertValueCount(1)
            .assertValue { result ->
                assertEquals(result.size, 2)
                val first = result[0]
                assertEquals(first.forecastInfo?.size, 6)
                val second = result[1]
                second.forecastInfo?.size == 3
            }
    }

    private fun stubMocks(list: List<ForecastInfo>) {
        val observable = Observable.just(list)
        whenever(remote.getAnnualForecastInfoByCity(any(), any())).thenReturn(observable)
    }
}