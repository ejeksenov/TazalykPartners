package kz.nextstep.domain.usecase.user

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.User
import kz.nextstep.domain.repository.UserRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetUserByIdUseCase(
    private val userRepository: UserRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<HashMap<String,User>, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<HashMap<String,User>> {
        return userRepository.getUserById(params)
    }
}