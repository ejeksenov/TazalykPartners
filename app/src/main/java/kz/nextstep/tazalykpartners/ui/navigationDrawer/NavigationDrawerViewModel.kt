package kz.nextstep.tazalykpartners.ui.navigationDrawer

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.usecase.partner.GetCurrentUserPartnerUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerByIdUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerIdUseCase
import kz.nextstep.domain.usecase.partner.SignOutUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class NavigationDrawerViewModel: BaseViewModel() {
    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase

    @Inject
    lateinit var signOutUseCase: SignOutUseCase

    val userPartnerLiveData = MutableLiveData<UserPartner>()
    val userPartnerPinIdsLiveData = MutableLiveData<String>()

    fun getCurrentUserPartner(){
        val mUserId = getUserPartnerIdUseCase.execute()
        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                userPartnerLiveData.value = t
                userPartnerPinIdsLiveData.value = t?.pinIds!!
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, mUserId, AppConstants.emptyParam)
    }

    fun signOut() = signOutUseCase.execute()


    override fun onCleared() {
        super.onCleared()
        getUserPartnerByIdUseCase.unsubscribe()
    }

}