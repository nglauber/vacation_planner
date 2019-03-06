package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.twvacationplanner.data.extensions.removeAccents
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import io.reactivex.Observable

open class WeatherTypeRepository(
    private val remote: Remote,
    private val cache: Cache<WeatherType>) {

    fun searchWeatherTypes(q: String): Observable<List<WeatherType>> {
        val network = remote.getWeatherTypes()
            .flatMap { weatherTypes ->
                cache.saveToCache(*weatherTypes.toTypedArray())
                    .andThen(Observable.just(weatherTypes))
            }

        return Observable.merge(cache.getFromCache(), network)
            .take(1)
            .map { weatherTypes ->
                weatherTypes.filter { weatherType ->
                    weatherType.name.removeAccents().contains(q, true)
                }
            }
    }
}