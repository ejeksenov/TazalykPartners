package kz.nextstep.tazalykpartners.base

import android.app.Application
import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DaggerAppComponent
import kz.nextstep.tazalykpartners.di.DataModule
import kz.nextstep.tazalykpartners.ui.login.LoginViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinListViewModel

open class BaseViewModel : ViewModel() {
    private val injector: AppComponent = DaggerAppComponent.builder().dataModule(MainApplication.INSTANCE?.let {
        DataModule(
            it
        )
    }).build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            //is AddPinViewModel -> injector.inject(this)
            is LoginViewModel -> injector.inject(this)
            is PinListViewModel -> injector.inject(this)
        }
    }

}