package kz.nextstep.tazalykpartners.ui.editProfile

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.nextstep.domain.model.User
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.usecase.partner.ChangeUserPartnerDataUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerByIdUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerIdUseCase
import kz.nextstep.domain.usecase.partner.SignOutUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class ChangeUserDataViewModel : BaseViewModel() {

    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase

    @Inject
    lateinit var changeUserPartnerDataUseCase: ChangeUserPartnerDataUseCase

    @Inject
    lateinit var signOutUseCase: SignOutUseCase

    val userPartnerLiveData = MutableLiveData<UserPartner>()
    val changeUserPartnerLiveData = MutableLiveData<Boolean>()

    fun getUserPartnerData() {
        val userId = getUserPartnerIdUseCase.execute()

        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                userPartnerLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, userId, AppConstants.emptyParam)
    }


    fun saveUserPartnerData(imageUrl: String, fullname: String) {
        changeUserPartnerDataUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                changeUserPartnerLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                changeUserPartnerLiveData.value = false
                showToastMessage(e?.message)
            }

        }, imageUrl, fullname)
    }
}
