package kz.nextstep.domain.usecase.partner

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.repository.UserPartnerRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class GetUserPartnerByIdUseCase(
    private val userPartnerRepository: UserPartnerRepository,
    @NonNull mainScheduler: Scheduler,
    @NonNull ioScheduler: Scheduler
): BaseUseCase<UserPartner, String, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: String, param2: String): Observable<UserPartner> {
        return userPartnerRepository.getUserPartnerById(params)
    }

}