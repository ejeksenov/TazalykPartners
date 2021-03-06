package kz.nextstep.tazalykpartners

import android.app.Application
import kz.nextstep.data.FirebaseHelper
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DaggerAppComponent
import kz.nextstep.tazalykpartners.di.DataModule
import kz.nextstep.tazalykpartners.utils.SharedPrefManager
import kz.nextstep.tazalykpartners.utils.TypefaceUtil
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder





class MainApplication: Application() {

    lateinit var appComponent: AppComponent
    companion object {
        var INSTANCE: MainApplication? = null
    }
    private var cicerone: Cicerone<Router>? = null

    override fun onCreate() {
        super.onCreate()

        inject()

        TypefaceUtil.overrideFont(applicationContext, "SERIF", "fonts/Montserrat-Regular.ttf")

        FirebaseHelper.setPersistenceEnabled()

        INSTANCE = this
        cicerone = Cicerone.create()
    }

    fun getNavigatorHolder(): NavigatorHolder {
        return cicerone!!.navigatorHolder
    }

    fun getRouter(): Router {
        return cicerone!!.router
    }

    private fun inject() {
        appComponent = DaggerAppComponent.builder().dataModule(DataModule(this)).build()

        appComponent.inject(this)
    }

    fun getApplicationComponent(): AppComponent {
        return appComponent
    }

    override fun onTerminate() {
        super.onTerminate()
        SharedPrefManager.saveSharedSetting(this, SharedPrefManager.SHARED_PREFS_FILE_NAME, "")
    }
}