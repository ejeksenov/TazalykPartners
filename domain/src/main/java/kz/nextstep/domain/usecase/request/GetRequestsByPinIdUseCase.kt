package kz.nextstep.domain.usecase.request

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.Requests
import kz.nextstep.domain.repository.RequestRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetRequestsByPinIdUseCase(
    private val requestRepository: RequestRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<HashMap<String,Requests>, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<HashMap<String,Requests>> {
        return requestRepository.getRequestsByPinId(params)
    }

}