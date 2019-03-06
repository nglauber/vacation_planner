package br.com.nglauber.twvacationplanner.domain.vacation

import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.VacationSuggestion
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.repository.ForecastInfoRepository
import br.com.nglauber.twvacationplanner.domain.ObservableUseCase
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import io.reactivex.Observable
import java.awt.SystemColor.info
import java.lang.IllegalArgumentException
import java.util.*

open class SearchVacationBestDatesUseCase(
    private val repository: ForecastInfoRepository,
    postExecutionThread: PostExecutionThread
) : ObservableUseCase<List<VacationSuggestion>, SearchVacationBestDatesUseCase.Params>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Observable<List<VacationSuggestion>> {
        return if (params == null) {
            Observable.error(IllegalArgumentException("You must provide the city id and year."))
        } else {
            return repository.getAnnualForecastInfoByCity(params.cityId, params.year)
                .flatMap { forecastInfoList ->
                    findBestDates(forecastInfoList, params)
                }
        }
    }

    private fun findBestDates(forecastInfoList: List<ForecastInfo>,
                              params: Params): Observable<List<VacationSuggestion>> {
        return Observable.just(forecastInfoList)
            .map { forecastInfoDates ->
                findBestVacationDates(forecastInfoDates, params)
            }
    }

    private fun findBestVacationDates(forecastInfoDates: List<ForecastInfo>,
                                      params: Params): List<VacationSuggestion> {
        val weatherTypeNames = params.weatherTypes.map { it.name }
        var daysCount = 0
        var startDate = Date()
        var endDate = Date()
        var forecastInfo = mutableListOf<ForecastInfo>()
        val result = mutableListOf<VacationSuggestion>()
        for (info in forecastInfoDates) {
            if (weatherTypeNames.contains(info.weather)) {
                if (daysCount == 0) {
                    forecastInfo = mutableListOf()
                    startDate = info.date
                } else {
                    endDate = info.date
                }
                forecastInfo.add(info)
                daysCount++
            } else {
                if (daysCount >= params.numberOfVacationDays) {
                    result.add(VacationSuggestion(startDate, endDate, forecastInfo))
                }
                daysCount = 0
            }
        }
        if (daysCount >= params.numberOfVacationDays) {
            result.add(VacationSuggestion(startDate, endDate, forecastInfo))
        }
        return result
    }

    data class Params(
        val cityId: String,
        val year: Int,
        val weatherTypes: List<WeatherType>,
        val numberOfVacationDays: Int = FIFTEEN_DAYS
    )

    companion object {
        const val FIFTEEN_DAYS = 15
        const val THIRTY_DAYS = 30
    }
}