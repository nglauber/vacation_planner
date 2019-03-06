package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import io.reactivex.Observable

open class ForecastInfoRepository(private val remote: Remote) {

    fun getAnnualForecastInfoByCity(cityId: String, year: Int): Observable<List<ForecastInfo>> {
        return remote.getAnnualForecastInfoByCity(cityId, year)
    }
}