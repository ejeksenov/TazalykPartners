package kz.nextstep.tazalykpartners.ui.statistics

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.usecase.historyPin.GetHistoryPinListUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.ChangeDateFormat
import kz.nextstep.domain.utils.ChangeDateFormat.isOnDate
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import kz.nextstep.tazalykpartners.utils.data.WasteItem
import rx.Subscriber
import java.math.RoundingMode
import javax.inject.Inject

class StatisticsViewModel : BaseViewModel() {

    val customProgressBarLiveData = MutableLiveData<Boolean>()

    @Inject
    lateinit var getHistoryPinListUseCase: GetHistoryPinListUseCase

    var wasteItemList: MutableList<WasteItem> = ArrayList()
    var filteredWasteItemList: MutableList<WasteItem> = ArrayList()

    private val totalPassedTextMutableLiveData = MutableLiveData<String>()
    private val averagePassedTextMutableLiveData = MutableLiveData<String>()

    val statisticsHistoryAdapter = StatisticsHistoryAdapter()

    fun getHistoryPinList(pinId: String, filterDateDays: Int, selectedDates: String, selectedWasteType: String) {
        getHistoryPinListUseCase.execute(object : Subscriber<HashMap<String, HistoryPin>>() {
            override fun onNext(t: HashMap<String, HistoryPin>?) {
                val historyPinList: MutableList<HistoryPin> = ArrayList()
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        historyPinList.add(t[key]!!)
                    }
                    getWasteItemList(filterDateDays, selectedDates, historyPinList, selectedWasteType)
                } else
                    noData()
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Toast.makeText(MainApplication.INSTANCE?.applicationContext, e?.message, Toast.LENGTH_LONG).show()
            }

        }, pinId, AppConstants.emptyParam)
    }

    fun getTotalPassed() = totalPassedTextMutableLiveData
    fun getAveragePassed() = averagePassedTextMutableLiveData


    fun getWasteItemList(
        filterDateDays: Int,
        selectedDates: String,
        historyPinMutableList: MutableList<HistoryPin>,
        selectedWasteType: String
    ) {
        var total = 0.0
        wasteItemList.clear()
        if (historyPinMutableList.isNotEmpty()) {
            for (historyPinItem in historyPinMutableList) {
                val passedDate = historyPinItem.time
                if (isOnDate(passedDate!!, selectedDates)) {
                    val totalArr =
                        historyPinItem.total?.split(";".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
                    if (totalArr != null) {
                        for (totalItem in totalArr) {
                            val totalItemArr =
                                totalItem.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (totalItemArr.size >= 3) {
                                val pinTotalNum = totalItemArr[2].toDouble()
                                wasteItemList.add(
                                    WasteItem(
                                        totalItemArr[0],
                                        totalItemArr[1],
                                        pinTotalNum,
                                        historyPinItem.userId,
                                        historyPinItem.time
                                    )
                                )
                                total += pinTotalNum
                            }
                        }
                    }
                }
            }
            if (wasteItemList.isEmpty()) {
                noData()
                showToastMessage(AppConstants.NO_DATA)
            } else {
                if (total > 0.0 && filterDateDays > 0) {
                    val averageTotal = (total / filterDateDays).toBigDecimal().setScale(3, RoundingMode.UP)
                    totalPassedTextMutableLiveData.value = "${total.toBigDecimal().setScale(3, RoundingMode.UP)} кг"
                    averagePassedTextMutableLiveData.value = "$averageTotal кг в среднем"
                }
                getWasteItemListByType(selectedWasteType, wasteItemList)
            }
        }
    }

    private fun noData() {
        customProgressBarLiveData.value = true
        statisticsHistoryAdapter.clearAllList()
        totalPassedTextMutableLiveData.value = "0 кг"
        averagePassedTextMutableLiveData.value = "0 кг в среднем"
    }

    private fun getWasteItemListByType(selectedWasteType: String, wasteItemList: MutableList<WasteItem>) {
        filteredWasteItemList.clear()
        if (wasteItemList.isNotEmpty()) {
            for (wasteItem in wasteItemList) {
                var isAddedItem = false
                if (wasteItem.selected_waste_type == selectedWasteType) {
                    for ((index, filterWasteItem) in filteredWasteItemList.withIndex()) {
                        if (wasteItem.selected_waste_id == filterWasteItem.selected_waste_id) {
                            val total = wasteItem.passed_total?.plus(filterWasteItem.passed_total!!)
                            val wasteUpdatedItem = WasteItem(
                                wasteItem.selected_waste_type,
                                wasteItem.selected_waste_id,
                                total,
                                wasteItem.userId,
                                wasteItem.passedDate
                            )
                            filteredWasteItemList[index] = wasteUpdatedItem
                            isAddedItem = true
                            break
                        }
                    }
                    if (!isAddedItem) filteredWasteItemList.add(wasteItem)
                }
            }
            filteredWasteItemList.sortByDescending { it.passed_total }
            statisticsHistoryAdapter.upDateStatisticsHistoryAdapter(wasteItemList, filteredWasteItemList)
            customProgressBarLiveData.value = true
        }

    }


    override fun onCleared() {
        super.onCleared()
        getHistoryPinListUseCase.unsubscribe()
    }
}
