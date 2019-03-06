package br.com.nglauber.twvacationplanner.data.remote.repository

import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.remote.parser.Parser
import br.com.nglauber.twvacationplanner.data.remote.service.RemoteWebService
import br.com.nglauber.twvacationplanner.data.repository.Remote
import io.reactivex.Observable

class RemoteRepository(
    private val service: RemoteWebService): Remote {

    override fun getCities(): Observable<List<City>> =
        service.getCities()
            .map { cities ->
                cities.map { Parser.toData(it) }
            }

    override fun getWeatherTypes(): Observable<List<WeatherType>> =
        service.getWeatherTypes()

    override fun getAnnualForecastInfoByCity(cityId: String, year: Int): Observable<List<ForecastInfo>> =
        service.getAnnualForecastInfoByCity(cityId, year)
            .map { fInfoList ->
                fInfoList.map { Parser.toData(it) }
            }
}