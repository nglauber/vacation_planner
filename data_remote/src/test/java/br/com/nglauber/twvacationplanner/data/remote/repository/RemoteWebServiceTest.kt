package br.com.nglauber.twvacationplanner.data.remote.repository

import br.com.nglauber.twvacationplanner.data.remote.service.RemoteWebService
import br.com.nglauber.twvacationplanner.data.remote.service.RemoteWebService.Companion.UNIT_TEST_URL
import br.com.nglauber.twvacationplanner.data.remote.service.RemoteWebServiceFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class RemoteWebServiceTest {

    private lateinit var service: RemoteWebService

    @Before
    fun init() {
        service = RemoteWebServiceFactory.create(baseUrl = UNIT_TEST_URL)
    }

    @Test
    fun assertCitiesAreRetrieved() {
        service.getCities()
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun assertWeatherTypesAreRetrieved() {
        service.getWeatherTypes()
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun assertDailyForecastInfoAreRetrieved() {
        service.getAnnualForecastInfoByCity("455821", 2019)
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun invalidCityMustCauseError() {
        service.getAnnualForecastInfoByCity("XPTO", 2019)
            .test()
            .assertError { err ->
                err is HttpException && err.code() == 404
            }
    }
}