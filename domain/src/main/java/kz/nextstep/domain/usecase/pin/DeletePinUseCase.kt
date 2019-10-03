package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import org.jetbrains.annotations.NotNull
import rx.Observable
import rx.Scheduler

class DeletePinUseCase(val pinRepository: PinRepository,
                       @NonNull mainScheduler: Scheduler,
                       @NonNull ioScheduler: Scheduler
): BaseUseCase<Boolean>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, pin: Pin): Observable<Boolean> {
        return pinRepository.deletePin(params)
    }
}