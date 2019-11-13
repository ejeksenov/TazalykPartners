package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.repository.PinRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class DeletePinUseCase(val pinRepository: PinRepository,
                       @NonNull mainScheduler: Scheduler,
                       @NonNull ioScheduler: Scheduler
): BaseUseCase<Boolean, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<Boolean> {
        return pinRepository.deletePin(params)
    }
}