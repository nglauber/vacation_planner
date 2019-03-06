package br.com.nglauber.twvacationplanner.domain.city

import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.repository.CityRepository
import br.com.nglauber.twvacationplanner.domain.ObservableUseCase
import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import io.reactivex.Observable

open class SearchCityUseCase(
    private val repository: CityRepository,
    postExecutionThread: PostExecutionThread
) : ObservableUseCase<List<City>, String>(postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<List<City>> {
        val q = params.toString().trim().toLowerCase()
        return repository.searchCities(q).map { list -> list.sortedBy { it.province } }
    }
}