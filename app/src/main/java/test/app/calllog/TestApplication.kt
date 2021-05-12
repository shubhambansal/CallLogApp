package test.app.calllog

import android.app.Application
import org.koin.android.ext.android.startKoin
import test.app.calllog.data.dataModule
import test.app.calllog.screens.viewModelModule
import timber.log.Timber

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val moduleList = listOf(
            dataModule, viewModelModule
        )

        startKoin(this, moduleList)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}