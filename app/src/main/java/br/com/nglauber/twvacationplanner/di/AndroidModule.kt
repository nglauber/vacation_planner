package br.com.nglauber.twvacationplanner.di

import br.com.nglauber.twvacationplanner.domain.PostExecutionThread
import br.com.nglauber.twvacationplanner.executor.UiThread
import org.koin.dsl.module.module


val androidModule = module {
    single { this }
    single { UiThread() as PostExecutionThread }
}