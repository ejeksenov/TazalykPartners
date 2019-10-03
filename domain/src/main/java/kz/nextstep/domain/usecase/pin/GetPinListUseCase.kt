package kz.nextstep.domain.usecase

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import rx.Observable
import rx.Scheduler

class GetPinListUseCase(val pinRepository: PinRepository,
                        @NonNull mainScheduler: Scheduler,
                        @NonNull ioScheduler: Scheduler
): BaseUseCase<List<Pin>>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, pin: Pin): Observable<List<Pin>> {
        return pinRepository.getPinList(params)
    }

}