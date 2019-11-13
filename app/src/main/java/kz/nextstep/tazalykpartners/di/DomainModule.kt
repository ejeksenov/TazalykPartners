package kz.nextstep.tazalykpartners.di

import dagger.Module
import dagger.Provides
import kz.nextstep.domain.repository.PinRepository
import kz.nextstep.domain.repository.*
import kz.nextstep.domain.usecase.historyPin.GetHistoryPinListUseCase
import kz.nextstep.domain.usecase.marking.GetMarkingListByTypeUseCase
import kz.nextstep.domain.usecase.partner.*
import kz.nextstep.domain.usecase.pin.AddPinUseCase
import kz.nextstep.domain.usecase.pin.GetPinListUseCase
import kz.nextstep.domain.usecase.pin.GetPinUseCase
import kz.nextstep.domain.usecase.pin.UpdatePinDataUseCase
import kz.nextstep.domain.usecase.pin.DeletePinUseCase
import kz.nextstep.domain.usecase.request.GetRequestsByPinIdUseCase
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
    @Provides
    fun provideGetUserByIdUseCase(
        userRepository: UserRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetUserByIdUseCase(userRepository, mainScheduler, ioScheduler)


    @Provides
    fun provideGetUserListByIdsUseCase(
        userRepository: UserRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetUserListByIdsUseCase(userRepository, mainScheduler, ioScheduler)


    //Providing User partner use cases
    @Provides
    fun provideGetCurrentUserPartnerUseCase(
        userPartnerRepository: UserPartnerRepository
    ) = GetCurrentUserPartnerUseCase(userPartnerRepository)

    @Provides
    fun provideGetUserPartnerByIdUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetUserPartnerByIdUseCase(userPartnerRepository, mainScheduler, ioScheduler)


    @Provides
    fun provideGetUserPartnerIdUseCase(
        userPartnerRepository: UserPartnerRepository
    ) = GetUserPartnerIdUseCase(userPartnerRepository)


    @Provides
    fun provideSignInWithEmailAndPasswordUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = SignInWithEmailAndPasswordUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideSignOutUseCase(
        userPartnerRepository: UserPartnerRepository
    ) = SignOutUseCase(userPartnerRepository)

    @Provides
    fun provideSendResetPasswordUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = SendResetPasswordUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideChangeUserPartnerDataUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = ChangeUserPartnerDataUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideChangeUserPartnerPasswordUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = ChangeUserPartnerPasswordUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideChangeUserPartnerEmailUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = ChangeUserPartnerEmailUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    @Provides
    fun provideChangeUserPartnerPinIdUseCase(
        userPartnerRepository: UserPartnerRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = ChangeUserPartnerPinIdUseCase(userPartnerRepository, mainScheduler, ioScheduler)

    // Providing requests use case
    @Provides
    fun provideGetRequestsByPinIdUseCase(
        requestRepository: RequestRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetRequestsByPinIdUseCase(requestRepository, mainScheduler, ioScheduler)


    // Providing markings use case
    @Provides
    fun provideGetMarkingListByTypeUseCase(
        markingRepository: MarkingRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetMarkingListByTypeUseCase(markingRepository, mainScheduler, ioScheduler)

    //Providing history pin use case
    @Provides
    fun provideGetHistoryPinListUseCase(
        historyPinRepository: HistoryPinRepository,
        @Named(RxModule.MAIN) mainScheduler: Scheduler,
        @Named(RxModule.IO) ioScheduler: Scheduler
    ) = GetHistoryPinListUseCase(historyPinRepository, mainScheduler, ioScheduler)

}