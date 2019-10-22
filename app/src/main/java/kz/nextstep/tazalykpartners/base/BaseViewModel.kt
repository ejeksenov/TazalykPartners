package kz.nextstep.tazalykpartners.base

import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DaggerAppComponent
import kz.nextstep.tazalykpartners.di.DataModule
import kz.nextstep.tazalykpartners.ui.login.LoginViewModel
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinDetailedInfoViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinTakeTypeViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.RequestViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinListViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinViewModel

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
            is NavigationDrawerViewModel -> injector.inject(this)
            is PinViewModel -> injector.inject(this)
            is PinDetailedInfoViewModel -> injector.inject(this)
            is PinTakeTypeViewModel -> injector.inject(this)
            is RequestViewModel -> injector.inject(this)
        }
    }

}