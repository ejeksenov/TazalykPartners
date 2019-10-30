package kz.nextstep.tazalykpartners.ui.editProfile

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.usecase.partner.ChangeUserPartnerPasswordUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class ChangePasswordViewModel : BaseViewModel() {

    @Inject
    lateinit var changeUserPartnerPasswordUseCase: ChangeUserPartnerPasswordUseCase

    val changePasswordLiveData = MutableLiveData<Boolean>()


    fun changeUserPartnerPassword(password: String, newPassword: String) {
        changeUserPartnerPasswordUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                changePasswordLiveData.value = t!!
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                changePasswordLiveData.value = false
                val message = e?.message
                if (message?.contains("password is invalid")!!)
                    showToastMessage(AppConstants.ERROR_INVALID_PASSWORD)
                else
                    showToastMessage(message)
            }

        }, password, newPassword)
    }

}
