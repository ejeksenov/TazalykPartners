package kz.nextstep.domain.usecase.historyPin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.repository.HistoryPinRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class AddHistoryPinUseCase(val historyPinRepository: HistoryPinRepository,
                           @NonNull mainScheduler: Scheduler,
                           @NonNull ioScheduler: Scheduler
): BaseUseCase<Boolean, HistoryPin, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: HistoryPin, param2: String): Observable<Boolean> {
        return historyPinRepository.addHistoryPin(params)
    }
}