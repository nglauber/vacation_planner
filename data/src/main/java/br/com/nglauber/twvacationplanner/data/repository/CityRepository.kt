package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.twvacationplanner.data.extensions.removeAccents
import br.com.nglauber.twvacationplanner.data.model.City
import io.reactivex.Observable

open class CityRepository(
    private val remote: Remote,
    private val cache: Cache<City>) {

    fun searchCities(q: String): Observable<List<City>> {
        val network = remote.getCities()
            .flatMap { cities ->
                cache.saveToCache(*cities.toTypedArray())
                    .andThen(Observable.just(cities))
            }

        return Observable.merge(cache.getFromCache(), network)
            .take(1)
            .map { cities ->
                cities.filter { city ->
                    city.district.removeAccents().contains(q, true)
                }
            }
    }
}