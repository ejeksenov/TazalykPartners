package kz.nextstep.domain.usecase

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.Pin
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.subscriptions.Subscriptions

abstract class BaseUseCase<Response, Arg1, Arg2>(@NonNull mainScheduler: Scheduler,
                                            @NonNull ioScheduler: Scheduler ) {

    private val mainScheduler: Scheduler = mainScheduler
    private val ioScheduler: Scheduler = ioScheduler

    private var subscription = Subscriptions.empty()


    protected abstract fun buildUseCaseObservable(params: Arg1, param2: Arg2): Observable<Response>

    @SuppressWarnings("unchecked")
    fun execute(useCaseSubscriber: Subscriber<Response>, params: Arg1, param2: Arg2) {
        this.subscription = this.buildUseCaseObservable(params, param2)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe(useCaseSubscriber)
    }

    fun unsubscribe() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

}