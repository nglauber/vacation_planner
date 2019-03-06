package br.com.nglauber.twvacationplanner.domain.weathertype

import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.repository.WeatherTypeRepository
import br.com.nglauber.twvacationplanner.domain.ObservableUseCase
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import io.reactivex.Observable

open class SearchWeatherTypeUseCase(
    private val repository: WeatherTypeRepository,
    postExecutionThread: PostExecutionThread
) : ObservableUseCase<List<WeatherType>, String>(postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<List<WeatherType>> {
        val q = params.toString().trim().toLowerCase()

        return repository.searchWeatherTypes(q).map { list -> list.sortedBy { it.name } }
    }
}