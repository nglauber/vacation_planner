package br.com.nglauber.twvacationplanner

import android.app.Application
import br.com.nglauber.twvacationplanner.di.androidModule
import br.com.nglauber.twvacationplanner.di.dataModule
import br.com.nglauber.twvacationplanner.di.domainModule
import br.com.nglauber.twvacationplanner.di.presentationModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this,
            listOf(
                androidModule,
                presentationModule,
                domainModule,
                dataModule
            )
        )
    }
}