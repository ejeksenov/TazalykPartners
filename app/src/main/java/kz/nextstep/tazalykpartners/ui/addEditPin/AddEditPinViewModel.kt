package kz.nextstep.tazalykpartners.ui.addEditPin

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.partner.ChangeUserPartnerPinIdUseCase
import kz.nextstep.domain.usecase.pin.AddPinUseCase
import kz.nextstep.domain.usecase.pin.GetPinUseCase
import kz.nextstep.domain.usecase.pin.UpdatePinDataUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class AddEditPinViewModel: BaseViewModel() {

    @Inject
    lateinit var getPinUseCase: GetPinUseCase

    @Inject
    lateinit var addPinUseCase: AddPinUseCase

    @Inject
    lateinit var updatePinDataUseCase: UpdatePinDataUseCase

    @Inject
    lateinit var changeUserPartnerPinIdUseCase: ChangeUserPartnerPinIdUseCase


    val pinMutableLiveData = MutableLiveData<Pin>()
    val newPinMutableLiveData = MutableLiveData<Boolean>()

    fun getPinData(pinId: String) {
        getPinUseCase.execute(object : Subscriber<HashMap<String, Pin>>(){
            override fun onNext(t: HashMap<String, Pin>?) {
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        pinMutableLiveData.value = t[key]
                    }
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }


    fun addNewPin(pin: Pin) {
        addPinUseCase.execute(object : Subscriber<String>() {
            override fun onNext(pinId: String?) {
                if (!pinId.isNullOrBlank()) {
                    onAddNewPinId(pinId)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, AppConstants.emptyParam, pin)
    }

    private fun onAddNewPinId(pinId: String) {
        changeUserPartnerPinIdUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                newPinMutableLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }


    fun updatePinData(pinId: String, pin: Pin) {
        updatePinDataUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                newPinMutableLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, pin)
    }
}