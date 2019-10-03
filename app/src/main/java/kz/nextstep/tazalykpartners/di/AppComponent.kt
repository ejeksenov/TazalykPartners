package kz.nextstep.tazalykpartners.di

import dagger.Component
import kz.nextstep.tazalykpartners.ui.MainActivity
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.ui.AddPinViewModel
import kz.nextstep.tazalykpartners.ui.GetPinByIdViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, RxModule::class])
interface AppComponent {
    fun inject(addPinViewModel: AddPinViewModel)
    fun inject(getPinByIdViewModel: GetPinByIdViewModel)
    fun inject(mainApplication: MainApplication)
    fun inject(mainActivity: MainActivity)
}