package kz.nextstep.tazalykpartners.ui.userInteractivity

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.User
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.tazalykpartners.utils.data.PassedUserPinItem
import kz.nextstep.domain.usecase.historyPin.GetHistoryPinListUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerByIdUseCase
import kz.nextstep.domain.usecase.partner.GetUserPartnerIdUseCase
import kz.nextstep.domain.usecase.pin.GetPinListUseCase
import kz.nextstep.domain.usecase.pin.GetPinUseCase
import kz.nextstep.domain.usecase.user.GetUserByIdUseCase
import kz.nextstep.domain.usecase.user.GetUserListByIdsUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.ChangeDateFormat.isOnDate
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import java.math.RoundingMode
import javax.inject.Inject

class UserInteractivityViewModel : BaseViewModel() {

    val customProgressBarLiveData = MutableLiveData<Boolean>()
    val passedPinUserListDatas = MutableLiveData<MutableList<PassedUserPinItem>>()

    @Inject
    lateinit var getHistoryPinListUseCase: GetHistoryPinListUseCase

    @Inject
    lateinit var getUserPartnerIdUseCase: GetUserPartnerIdUseCase

    @Inject
    lateinit var getUserPartnerByIdUseCase: GetUserPartnerByIdUseCase


    @Inject
    lateinit var getUserListByIdsUseCase: GetUserListByIdsUseCase

    @Inject
    lateinit var getPinListUseCase: GetPinListUseCase

    val userInteractivityAdapter = UserInteractivityAdapter()

    fun getPassedUserList(wasteId: String, selectedDate: String) {
        val userPartnerId = getUserPartnerIdUseCase.execute()

        getUserPartnerByIdUseCase.execute(object : Subscriber<UserPartner>() {
            override fun onNext(t: UserPartner?) {
                if (t != null) {
                    val pinIds = t.pinIds!!
                    onGetHistoryPinList(pinIds, wasteId, selectedDate)
                }

            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, userPartnerId, AppConstants.emptyParam)
    }

    private fun onGetHistoryPinList(pinIds: String, wasteId: String, selectedDate: String) {
        getHistoryPinListUseCase.execute(object : Subscriber<HashMap<String, HistoryPin>>() {
            override fun onNext(t: HashMap<String, HistoryPin>?) {
                val passedUserItemMutableList: MutableList<PassedUserPinItem> = ArrayList()
                var filteredPinIds = ""
                var filteredUserIds = ""
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        val historyPin = t[key]
                        if (historyPin != null) {
                            val passedDate = historyPin.time
                            if (passedDate != null && (selectedDate.isBlank() || isOnDate(passedDate, selectedDate))) {
                                val passedTotal = historyPin.total
                                if (!passedTotal.isNullOrBlank()) {
                                    for (wasteIdItem in wasteId.split(";")) {
                                        val wasteIdItemArr = wasteIdItem.split(",")

                                        if (wasteId.isBlank() || (wasteIdItemArr.size >= 5 && passedTotal.contains(
                                                wasteIdItemArr[1]
                                            ))
                                        ) {
                                            val itemPinId = historyPin.pinId
                                            val itemUserId = historyPin.userId
                                            val totalStr = onGetTotalSum(historyPin.total!!)

                                            filteredPinIds += "$itemPinId,"
                                            filteredUserIds += "$itemUserId,"

                                            passedUserItemMutableList.add(
                                                PassedUserPinItem(
                                                    itemUserId,
                                                    itemPinId,
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    historyPin.total,
                                                    "",
                                                    historyPin.time,
                                                    totalStr
                                                )
                                            )
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (passedUserItemMutableList.isEmpty()) {
                        noData()
                        showToastMessage(AppConstants.NO_DATA)
                    } else
                        onGetPassedUserList(passedUserItemMutableList, filteredPinIds, filteredUserIds)
                } else
                    noData()
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinIds, AppConstants.emptyParam)
    }

    private fun onGetTotalSum(total: String): String {
        var totalSum = 0.0
        for (item in total.split(";")) {
            if (item.contains(",")) {
                val itemArr = item.split(",")
                if (itemArr.size >= 3) {
                    totalSum += itemArr[2].toDouble()
                }
            }
        }
        totalSum = totalSum.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
        return totalSum.toString()
    }

    private fun noData() {
        userInteractivityAdapter.clearAllList()
        customProgressBarLiveData.value = true
    }

    private fun onGetPassedUserList(
        passedUserItemMutableList: MutableList<PassedUserPinItem>,
        filteredPinIds: String,
        filteredUserIds: String
    ) {

        getUserListByIdsUseCase.execute(object : Subscriber<HashMap<String, User>>() {
            override fun onNext(t: HashMap<String, User>?) {
                if (!t.isNullOrEmpty()) {
                    for (item in passedUserItemMutableList) {
                        val userId = item.userId
                        if (t.containsKey(userId)) {
                            val user = t[userId]
                            if (user != null) {
                                item.userName = user.userName
                                item.userImageUrl = user.imageLink
                                item.userPhone = user.phoneNumber
                                item.userEmail = user.email
                            }
                        }
                    }
                    onGetPassedPinList(passedUserItemMutableList, filteredPinIds)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, filteredUserIds, AppConstants.emptyParam)
    }

    private fun onGetPassedPinList(
        passedUserItemMutableList: MutableList<PassedUserPinItem>,
        filteredPinIds: String
    ) {
        getPinListUseCase.execute(object : Subscriber<HashMap<String, Pin>>() {
            override fun onNext(t: HashMap<String, Pin>?) {
                if (!t.isNullOrEmpty()) {

                    for (item in passedUserItemMutableList) {
                        val pinId = item.pinId
                        if (t.containsKey(pinId)) {
                            val pin = t[pinId]
                            if (pin != null) {
                                item.pinAddress = pin.address
                            }
                        }
                    }
                    customProgressBarLiveData.value = true
                    userInteractivityAdapter.updatePassedUserPinList(passedUserItemMutableList)
                    passedPinUserListDatas.value = passedUserItemMutableList
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, filteredPinIds, AppConstants.emptyParam)

    }


    override fun onCleared() {
        super.onCleared()
        getUserPartnerByIdUseCase.unsubscribe()
        getHistoryPinListUseCase.unsubscribe()
        getUserListByIdsUseCase.unsubscribe()
        getPinListUseCase.unsubscribe()
    }
}
