package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.repository.PinRepository
import kz.nextstep.domain.usecase.BaseUseCase
import rx.Observable
import rx.Scheduler

class UploadPinImagesUseCase(val pinRepository: PinRepository,
                             @NonNull mainScheduler: Scheduler,
                             @NonNull ioScheduler: Scheduler
): BaseUseCase<String, HashMap<String, String>, String>(mainScheduler, ioScheduler) {
    override fun buildUseCaseObservable(params: HashMap<String, String>, param2: String): Observable<String> {
        return pinRepository.uploadPinImages(params, param2)
    }
}