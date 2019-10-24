package kz.nextstep.domain.usecase.historyPin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.repository.HistoryPinRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetHistoryPinListUseCase(val historyPinRepository: HistoryPinRepository,
                               @NonNull mainScheduler: Scheduler,
                               @NonNull ioScheduler: Scheduler
): BaseUseCase<HashMap<String,HistoryPin>, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<HashMap<String,HistoryPin>> {
        return historyPinRepository.getHistoryPinList(params)
    }
}