package kz.nextstep.domain.usecase

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.pin.BaseUseCase
import org.jetbrains.annotations.NotNull
import rx.Observable
import rx.Scheduler

class GetPinUseCase(val pinRepository: PinRepository,
                    @NonNull mainScheduler: Scheduler,
                    @NonNull ioScheduler: Scheduler
): BaseUseCase<Pin>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, pin: Pin): Observable<Pin> {
        return pinRepository.getPinById(params)
    }
}