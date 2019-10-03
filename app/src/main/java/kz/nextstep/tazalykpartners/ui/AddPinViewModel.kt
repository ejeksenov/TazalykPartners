package kz.nextstep.tazalykpartners.ui

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.AddPinUseCase
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class AddPinViewModel(mainApplication: MainApplication) : BaseViewModel(mainApplication) {

    @Inject
    lateinit var addPinUseCase: AddPinUseCase



    var message = MutableLiveData<String>()
    fun bound(pin: Pin) {
        addPinUseCase.execute(
            object : Subscriber<Boolean>() {
                override fun onNext(t: Boolean?) {
                    message.value = t.toString()
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    message.value = e?.message
                }

            }, "",
            pin
        )
    }

    override fun onCleared() {
        super.onCleared()
        addPinUseCase.unsubscribe()
    }

}
