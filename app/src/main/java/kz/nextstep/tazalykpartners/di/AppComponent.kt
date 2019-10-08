package kz.nextstep.tazalykpartners.di

import dagger.Component
import kz.nextstep.tazalykpartners.ui.MainActivity
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.ui.login.LoginViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, RxModule::class])
interface AppComponent {
    //fun inject(addPinViewModel: AddPinViewModel)
    fun inject(mainApplication: MainApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(pinListViewModel: PinListViewModel)
}