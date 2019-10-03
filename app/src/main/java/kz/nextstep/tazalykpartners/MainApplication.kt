package kz.nextstep.tazalykpartners

import android.app.Application
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DataModule

class MainApplication: Application() {

    //lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        //inject()
    }

    /*private fun inject() {
        appComponent = DaggerAppComponent.builder().dataModule(DataModule(this)).build()

        appComponent.inject(this)
    }

    public fun getApplicationComponent(): AppComponent {
        return appComponent
    }*/
}