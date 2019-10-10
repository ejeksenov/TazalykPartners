package kz.nextstep.tazalykpartners.ui.navigationDrawer

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.usecase.partner.GetCurrentUserPartnerUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerByIdUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerIdUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class NavigationDrawerViewModel: BaseViewModel() {
    @Inject
    lateinit var getCurrentUserPartnerUseCase: GetCurrentUserPartnerUseCase

    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase

    val userPartnerLiveData = MutableLiveData<UserPartner>()


    fun getCurrentUser(): Boolean {
        return getCurrentUserPartnerUseCase.execute()
    }

    fun getCurrentUserPartner() {
        val mUserId = getUserPartnerIdUseCase.execute()

        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                userPartnerLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Toast.makeText(MainApplication.INSTANCE?.applicationContext, e?.message, Toast.LENGTH_SHORT).show()
            }

        }, mUserId, AppConstants.emptyParam)
    }
}