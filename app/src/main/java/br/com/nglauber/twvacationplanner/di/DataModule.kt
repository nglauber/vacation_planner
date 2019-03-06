package br.com.nglauber.twvacationplanner.di

import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.data.remote.repository.RemoteRepository
import br.com.nglauber.twvacationplanner.data.remote.service.RemoteWebServiceFactory
import br.com.nglauber.twvacationplanner.data.repository.*
import br.com.nglauber.twvacationplanner.local.MemoryCache
import org.koin.dsl.module.module

const val CITY_CACHE_NAME = "CityCache"
const val WEATHER_TYPE_CACHE_NAME = "WeatherCache"

val dataModule = module {

    single {
        RemoteWebServiceFactory.create(isDebug = true)
    }

    single {
        RemoteRepository(service = get()) as Remote
    }

    single {
        CityRepository(remote = get(), cache = get(name = CITY_CACHE_NAME))
    }

    single {
        WeatherTypeRepository(remote = get(), cache = get(name = WEATHER_TYPE_CACHE_NAME))
    }

    single {
        ForecastInfoRepository(remote = get())
    }

    single (name = CITY_CACHE_NAME) {
        MemoryCache<City>() as Cache<City>
    }

    single (name = WEATHER_TYPE_CACHE_NAME) {
        MemoryCache<WeatherType>() as Cache<WeatherType>
    }
}