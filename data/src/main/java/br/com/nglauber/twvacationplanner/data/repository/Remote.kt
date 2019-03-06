package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import io.reactivex.Observable

interface Remote {

    fun getCities(): Observable<List<City>>

    fun getWeatherTypes(): Observable<List<WeatherType>>

    fun getAnnualForecastInfoByCity(cityId: String, year: Int): Observable<List<ForecastInfo>>
}