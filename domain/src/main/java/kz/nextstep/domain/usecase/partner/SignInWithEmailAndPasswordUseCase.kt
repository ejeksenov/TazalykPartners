package kz.nextstep.domain.usecase.partner

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.repository.UserPartnerRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class SignInWithEmailAndPasswordUseCase(
    private val userPartnerRepository: UserPartnerRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<String, String, String>(mainScheduler, ioScheduler)  {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<String> {
        return userPartnerRepository.signInWithEmailAndPassword(params, param2)
    }
}