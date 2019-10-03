package kz.nextstep.tazalykpartners.di

import dagger.Module
import dagger.Provides
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.repository.UserRepository
import kz.nextstep.domain.usecase.AddPinUseCase
import kz.nextstep.domain.usecase.GetPinListUseCase
import kz.nextstep.domain.usecase.GetPinUseCase
import kz.nextstep.domain.usecase.UpdatePinDataUseCase
import kz.nextstep.domain.usecase.pin.DeletePinUseCase
import kz.nextstep.domain.usecase.user.GetUserByIdUseCase
import kz.nextstep.domain.usecase.user.GetUserListByIdsUseCase
import rx.Scheduler
import javax.inject.Named

@Module
class DomainModule {


    //Providing Pin use cases
    @Provides
    fun provideAddPinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = AddPinUseCase(pinRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideDeletePinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = DeletePinUseCase(pinRepository, mainScheduler, ioScheduler)


    @Provides
    fun provideGetPinListUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetPinListUseCase(pinRepository, mainScheduler, ioScheduler)


    @Provides
    fun provideGetPinUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetPinUseCase(pinRepository, mainScheduler, ioScheduler)


    @Provides
    fun provideUpdatePinDataUseCase(
        pinRepository: PinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = UpdatePinDataUseCase(pinRepository, mainScheduler, ioScheduler)


    //Providing User use cases
    fun provideGetUserByIdUseCase(
        userRepository: UserRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetUserByIdUseCase(userRepository, mainScheduler, ioScheduler)


    fun provideGetUserListByIdsUseCase(
        userRepository: UserRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetUserListByIdsUseCase(userRepository, mainScheduler, ioScheduler)

}