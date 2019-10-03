package kz.nextstep.domain.usecase.user

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.User
import kz.nextstep.domain.repository.UserRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetUserListByIdsUseCase(
    private val userRepository: UserRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<List<User>>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, pin: Pin): Observable<List<User>> {
        return userRepository.getUserListByIds(params)
    }
}