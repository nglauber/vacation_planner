package br.com.nglauber.twvacationplanner.di

import br.com.nglauber.twvacationplanner.presentation.VacationPlannerViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val presentationModule = module {
    viewModel {
        VacationPlannerViewModel(
            searchCityUseCase = get(),
            searchWeatherTypeUseCase = get(),
            searchVacationUseCase = get()
        )
    }
}