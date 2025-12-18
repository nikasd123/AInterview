package com.owl.ainterview

import android.app.Application
import com.owl.ainterview.di.appModule
import com.owl.data.di.dataModule
import com.owl.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class AIInterviewApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AIInterviewApp)

            modules(
                domainModule,
                dataModule,
                appModule
            )
        }
    }
}