package kz.nextstep.tazalykpartners.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.usecase.partner.*
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class LoginViewModel : BaseViewModel(){

    private val TAG = LoginActivity::class.java.name

    @Inject
    lateinit var signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase

    @Inject
    lateinit var sendResetPasswordUseCase: SendResetPasswordUseCase

    @Inject
    lateinit var getCurrentUserPartnerUseCase: GetCurrentUserPartnerUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase

    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase


    fun getCurrentUser(): Boolean {
        return getCurrentUserPartnerUseCase.execute()
    }

    val signInResultLiveData = MutableLiveData<Any>()
    val userRoleLiveData = MutableLiveData<String>()

    fun getUserRole() {
        val mUserId = getUserPartnerIdUseCase.execute()
        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                val pinIds = t?.pinIds
                val productIds = t?.productIds
                if (pinIds?.contains(",")!!)
                    userRoleLiveData.value = AppConstants.SUCCESS_PIN_DIRECTOR
                else if (pinIds != "" && !pinIds.contains(","))
                    userRoleLiveData.value = AppConstants.SUCCESS_PIN_ADMIN
                else if (productIds != "")
                    userRoleLiveData.value = AppConstants.SUCCESS_PRODUCT_SPONSOR
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                userRoleLiveData.value = e?.message
            }

        }, mUserId, AppConstants.emptyParam)
    }

    fun signIn(emailStr: String, passwordStr: String) {
        signInWithEmailAndPasswordUseCase.execute(object : Subscriber<String>() {
            override fun onNext(t: String?) {
                signInResultLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                signInResultLiveData.value = e?.message
            }
        }, emailStr, passwordStr)
    }


    fun sendResetPasswordEmail(emailStr: String) {
        sendResetPasswordUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                if (t!!)
                    showToastMessage("Сообщение успешно отправлено. Пожалуйста, проверьте почту")
                else
                    showToastMessage("Не удалось отправить сообщение!")
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message!!)
            }

        }, emailStr, AppConstants.emptyParam)
    }

    fun showToastMessage(message: String) {
        Toast.makeText(MainApplication.INSTANCE?.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
