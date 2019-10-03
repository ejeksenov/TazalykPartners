package kz.nextstep.tazalykpartners.ui

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.GetPinUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class GetPinByIdViewModel(mainApplication: MainApplication): BaseViewModel(mainApplication) {

    @Inject
    lateinit var getPinUseCase: GetPinUseCase

    val mutabLiveData = MutableLiveData<String>()

    fun bound(pinId: String) {
        getPinUseCase.execute(
            object : Subscriber<Pin>() {
                override fun onNext(t: Pin?) {
                    mutabLiveData.value = t?.address
                }

                override fun onCompleted() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(e: Throwable?) {
                    mutabLiveData.value = e?.message
                }

            }
        , pinId, AppConstants.emptyPin)
    }
}