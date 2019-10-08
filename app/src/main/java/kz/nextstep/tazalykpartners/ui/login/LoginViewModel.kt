package kz.nextstep.tazalykpartners.ui.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.usecase.partner.SendResetPasswordUseCase
import kz.nextstep.domain.usecase.partner.SignInWithEmailAndPasswordUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class LoginViewModel : BaseViewModel(){

    private val TAG = LoginFragment::class.java.name

    @Inject
    lateinit var signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase

    @Inject
    lateinit var sendResetPasswordUseCase: SendResetPasswordUseCase

    val signInResultLiveData = MutableLiveData<Any>()

    fun signIn(emailStr: String, passwordStr: String) {
        signInWithEmailAndPasswordUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                if (t!!) {
                    signInResultLiveData.value = true
                } else {
                    signInResultLiveData.value = R.string.wrongLoginOrPassword
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.e(TAG, e?.message.toString())
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
