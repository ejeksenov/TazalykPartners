package kz.nextstep.tazalykpartners.di

import dagger.Module
import dagger.Provides
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.usecase.AddPinUseCase
import kz.nextstep.domain.usecase.GetPinListUseCase
import kz.nextstep.domain.usecase.GetPinUseCase
import kz.nextstep.domain.usecase.UpdatePinDataUseCase
import kz.nextstep.domain.usecase.pin.DeletePinUseCase
import rx.Scheduler
import javax.inject.Named

@Module
class DomainModule {

    @Provides
    fun provideAddPinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ): AddPinUseCase {
        return AddPinUseCase(pinRepository, mainScheduler, ioScheduler)
    }

    @Provides
    fun provideDeletePinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ): DeletePinUseCase {
        return DeletePinUseCase(pinRepository, mainScheduler, ioScheduler)
    }


    @Provides
    fun provideGetPinListUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ): GetPinListUseCase {
        return GetPinListUseCase(pinRepository, mainScheduler, ioScheduler)
    }


    @Provides
    fun provideGetPinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ): GetPinUseCase {
        return GetPinUseCase(pinRepository, mainScheduler, ioScheduler)
    }


    @Provides
    fun provideUpdatePinDataUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ): UpdatePinDataUseCase {
        return UpdatePinDataUseCase(pinRepository, mainScheduler, ioScheduler)
    }

}