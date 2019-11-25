package kz.nextstep.tazalykpartners.ui.adminProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.User
import kz.nextstep.domain.usecase.pin.GetPinUseCase
import kz.nextstep.domain.usecase.user.GetUserByEmailUseCase
import kz.nextstep.domain.usecase.user.GetUserByPhoneUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class AdminProfileViewModel : BaseViewModel() {

    @Inject
    lateinit var getPinUseCase: GetPinUseCase

    @Inject
    lateinit var getUserByEmailUseCase: GetUserByEmailUseCase

    @Inject
    lateinit var getUserByPhoneUseCase: GetUserByPhoneUseCase

    val pinMutableLiveData = MutableLiveData<Pin>()
    val userMutableLiveData = MutableLiveData<String>()

    fun getPinById(pinId: String) {
        getPinUseCase.execute(object : Subscriber<HashMap<String, Pin>>() {
            override fun onNext(t: HashMap<String, Pin>?) {
                if (!t.isNullOrEmpty()) {
                    for(key in t.keys) {
                        pinMutableLiveData.value = t[key]
                    }
                } else
                    showToastMessage(AppConstants.ERROR_PIN_NOT_FOUND)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }

    fun getUserByEmail(email: String) {
        getUserByEmailUseCase.execute(object : Subscriber<HashMap<String, User>> () {
            override fun onNext(t: HashMap<String, User>?) {
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        userMutableLiveData.value = key
                    }
                } else {
                    showToastMessage(AppConstants.ERROR_USER_NOT_FOUND)
                    userMutableLiveData.value = null
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, email, AppConstants.emptyParam)
    }

    fun getUserByPhone(phone: String) {
        getUserByPhoneUseCase.execute(object : Subscriber<HashMap<String, User>> () {
            override fun onNext(t: HashMap<String, User>?) {
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        userMutableLiveData.value = key
                    }
                } else {
                    showToastMessage(AppConstants.ERROR_USER_NOT_FOUND)
                    userMutableLiveData.value = null
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, phone, AppConstants.emptyParam)
    }

    override fun onCleared() {
        super.onCleared()
        getPinUseCase.unsubscribe()
        getUserByEmailUseCase.unsubscribe()
        getUserByPhoneUseCase.unsubscribe()
    }
}
