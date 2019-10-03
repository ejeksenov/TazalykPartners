package kz.nextstep.domain.usecase.pin

import io.reactivex.annotations.NonNull
import kz.nextstep.domain.model.Pin
import org.jetbrains.annotations.NotNull
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.subscriptions.Subscriptions

public abstract class BaseUseCase<Response>(@NonNull mainScheduler: Scheduler,
                                            @NonNull ioScheduler: Scheduler ) {

    private val mainScheduler: Scheduler = mainScheduler
    private val ioScheduler: Scheduler = ioScheduler

    private var subscription = Subscriptions.empty()


    protected abstract fun buildUseCaseObservable(params: String, pin: Pin): Observable<Response>

    @SuppressWarnings("unchecked")
    public fun execute(useCaseSubscriber: Subscriber<Response>, params: String, pin: Pin) {
        this.subscription = this.buildUseCaseObservable(params, pin)
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