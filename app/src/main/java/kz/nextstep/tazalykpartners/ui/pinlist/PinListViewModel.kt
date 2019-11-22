package kz.nextstep.tazalykpartners.ui.pinlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.usecase.partner.GetUserPartnerByIdUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerIdUseCase
import kz.nextstep.domain.usecase.pin.GetPinListUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class PinListViewModel : BaseViewModel() {

    val TAG = PinListFragment::class.java.name

    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase

    @Inject
    lateinit var getPinListUseCase: GetPinListUseCase

    val pinListAdapter = PinListAdapter()


    fun getUserPartnerPinIds(filterTypes: String) {
        val userPartnerId = getUserPartnerIdUseCase.execute()

        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                getPinLists(t?.pinIds!!, filterTypes)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Log.e(TAG, e?.message!!)
            }

        }, userPartnerId, AppConstants.emptyParam)
    }

    fun getPinLists(pinIds: String, filterTypes: String) {

        getPinListUseCase.execute(object : Subscriber<HashMap<String, Pin>>() {
            override fun onNext(t: HashMap<String, Pin>?) {
                if (t.isNullOrEmpty()) {
                    pinListAdapter.clearAll()
                    showToastMessage(AppConstants.NO_DATA)
                } else
                    pinListAdapter.updatePinList(t)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Log.e(TAG, e?.message!!)
            }

        }, pinIds, filterTypes)
    }

    override fun onCleared() {
        super.onCleared()
        getUserPartnerByIdUseCase.unsubscribe()
        getPinListUseCase.unsubscribe()
    }
}
