package kz.nextstep.tazalykpartners.base

import android.app.Application
import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DaggerAppComponent
import kz.nextstep.tazalykpartners.di.DataModule
import kz.nextstep.tazalykpartners.ui.AddPinViewModel
import kz.nextstep.tazalykpartners.ui.GetPinByIdViewModel

open class BaseViewModel(mainApplication: MainApplication) : ViewModel() {
    private val injector: AppComponent = DaggerAppComponent.builder().dataModule(DataModule(mainApplication)).build()

    init {
        inject()
    }

    private fun inject() {
        when(this) {
            is AddPinViewModel -> injector.inject(this)
            is GetPinByIdViewModel -> injector.inject(this)
        }
    }
}