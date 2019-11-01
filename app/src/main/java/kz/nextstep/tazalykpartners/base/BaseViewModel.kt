package kz.nextstep.tazalykpartners.base

import android.widget.Toast
import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.di.AppComponent
import kz.nextstep.tazalykpartners.di.DaggerAppComponent
import kz.nextstep.tazalykpartners.di.DataModule
import kz.nextstep.tazalykpartners.ui.editProfile.ChangeEmailViewModel
import kz.nextstep.tazalykpartners.ui.editProfile.ChangePasswordViewModel
import kz.nextstep.tazalykpartners.ui.editProfile.ChangeUserDataViewModel
import kz.nextstep.tazalykpartners.ui.login.LoginViewModel
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerViewModel
import kz.nextstep.tazalykpartners.ui.pinComments.PinCommentsViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinDetailedInfoViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinTakeTypeViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.RequestViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinListViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinViewModel
import kz.nextstep.tazalykpartners.ui.passedUserList.StatisticsPassedUserListViewModel
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsViewModel
import kz.nextstep.tazalykpartners.ui.userInteractivity.UserInteractivityViewModel

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
            is PinCommentsViewModel -> injector.inject(this)
            is StatisticsViewModel -> injector.inject(this)
            is StatisticsPassedUserListViewModel -> injector.inject(this)
            is ChangeUserDataViewModel -> injector.inject(this)
            is ChangePasswordViewModel -> injector.inject(this)
            is ChangeEmailViewModel -> injector.inject(this)
            is UserInteractivityViewModel -> injector.inject(this)
        }
    }

    fun showToastMessage(message: String?) {
        Toast.makeText(MainApplication.INSTANCE?.applicationContext, message, Toast.LENGTH_LONG).show()
    }

}