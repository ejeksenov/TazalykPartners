package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.repository.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class AddPinUseCase(val pinRepository: PinRepository,
@NonNull mainScheduler: Scheduler,
@NonNull ioScheduler: Scheduler): BaseUseCase<String, String, Pin>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: Pin): Observable<String> {
        return pinRepository.addPin(param2)
    }
}