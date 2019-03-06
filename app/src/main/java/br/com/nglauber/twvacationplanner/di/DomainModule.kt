package br.com.nglauber.twvacationplanner.di

import br.com.nglauber.twvacationplanner.domain.city.SearchCityUseCase
import br.com.nglauber.twvacationplanner.domain.vacation.SearchVacationBestDatesUseCase
import br.com.nglauber.twvacationplanner.domain.weathertype.SearchWeatherTypeUseCase
import org.koin.dsl.module.module

val domainModule = module {
    factory {
        SearchCityUseCase(
            repository = get(),
            postExecutionThread = get()
        )
    }
    factory {
        SearchWeatherTypeUseCase(
            repository = get(),
            postExecutionThread = get()
        )
    }
    factory {
        SearchVacationBestDatesUseCase(
            repository = get(),
            postExecutionThread = get()
        )
    }
}