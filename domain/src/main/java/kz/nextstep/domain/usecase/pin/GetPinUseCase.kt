package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetPinUseCase(val pinRepository: PinRepository,
                    @NonNull mainScheduler: Scheduler,
                    @NonNull ioScheduler: Scheduler
): BaseUseCase<Pin, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<Pin> {
        return pinRepository.getPinById(params)
    }
}