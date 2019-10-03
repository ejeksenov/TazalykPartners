package kz.nextstep.domain.usecase

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import rx.Observable
import rx.Scheduler

class UpdatePinDataUseCase(val pinRepository: PinRepository,
                           @NonNull mainScheduler: Scheduler,
                           @NonNull ioScheduler: Scheduler
): BaseUseCase<Boolean>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, pin: Pin): Observable<Boolean> {
        return pinRepository.updatePinData(params, pin)
    }
}