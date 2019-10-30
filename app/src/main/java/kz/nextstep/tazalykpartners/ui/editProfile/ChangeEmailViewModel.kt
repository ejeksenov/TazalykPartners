package kz.nextstep.tazalykpartners.ui.editProfile

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.usecase.partner.ChangeUserPartnerEmailUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class ChangeEmailViewModel : BaseViewModel() {

    @Inject
    lateinit var changeUserPartnerEmailUseCase: ChangeUserPartnerEmailUseCase

    val changeEmailLiveData = MutableLiveData<Boolean>()

    fun changeEmail(newEmail: String, password: String) {
        changeUserPartnerEmailUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                changeEmailLiveData.value = t!!
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                changeEmailLiveData.value = false
                val message = e?.message
                if (message?.contains("password is invalid")!!)
                    showToastMessage(AppConstants.ERROR_INVALID_PASSWORD)
                else
                    showToastMessage(message)
            }

        }, password, newEmail)
    }
}
