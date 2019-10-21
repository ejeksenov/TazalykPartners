package kz.nextstep.domain.usecase.marking

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.Marking
import kz.nextstep.domain.repository.MarkingRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetMarkingListByTypeUseCase(
    private val markingRepository: MarkingRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<HashMap<String, Marking>, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<HashMap<String, Marking>> {
        return markingRepository.getMarkingListByType(params, param2)
    }
}