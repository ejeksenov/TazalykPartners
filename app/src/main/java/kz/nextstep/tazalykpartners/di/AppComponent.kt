package kz.nextstep.tazalykpartners.di

import dagger.Component
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.ui.login.LoginViewModel
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerViewModel
import kz.nextstep.tazalykpartners.ui.pinComments.PinCommentsViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinDetailedInfoViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinTakeTypeViewModel
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.RequestViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinListViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinViewModel
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, RxModule::class])
interface AppComponent {
    //fun inject(addPinViewModel: AddPinViewModel)
    fun inject(mainApplication: MainApplication)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(pinListViewModel: PinListViewModel)
    fun inject(pinViewModel: PinViewModel)
    fun inject(navigationDrawerViewModel: NavigationDrawerViewModel)
    fun inject(pinDetailedInfoViewModel: PinDetailedInfoViewModel)
    fun inject(pinTakeTypeViewModel: PinTakeTypeViewModel)
    fun inject(requestViewModel: RequestViewModel)
    fun inject(pinCommentsViewModel: PinCommentsViewModel)
    fun inject(statisticsViewModel: StatisticsViewModel)
}