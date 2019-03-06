package br.com.nglauber.twvacationplanner.data.remote.service

import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.remote.model.City
import br.com.nglauber.twvacationplanner.data.remote.model.ForecastInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteWebService {
    @GET(ACTION_GET_CITIES)
    fun getCities(): Observable<List<City>>

    @GET(ACTION_GET_WEATHER_TYPES)
    fun getWeatherTypes(): Observable<List<WeatherType>>

    @GET(ACTION_GET_ANNUAL_FORECAST_BY_CITY)
    fun getAnnualForecastInfoByCity(
        @Path(PATH_CITY_ID) cityId: String,
        @Path(PATH_YEAR) year: Int
    ): Observable<List<ForecastInfo>>

    companion object {
        private const val PORT = "8882"
        // For Android emulator use "http://10.0.2.2:$PORT/"
        // For Genymotion emulator use "http://10.0.3.2:$PORT/"
        const val API_BASE_URL = "http://10.0.3.2:$PORT/"

        const val UNIT_TEST_URL = "http://localhost:$PORT/"

        private const val PATH_CITY_ID = "cityId"
        private const val PATH_YEAR = "year"

        private const val ACTION_GET_CITIES = "cities/"
        private const val ACTION_GET_WEATHER_TYPES = "weather/"
        private const val ACTION_GET_ANNUAL_FORECAST_BY_CITY = "cities/{$PATH_CITY_ID}/year/{$PATH_YEAR}"
    }
}