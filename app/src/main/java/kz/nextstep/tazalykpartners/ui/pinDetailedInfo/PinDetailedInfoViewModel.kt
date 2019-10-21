package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.pin.GetPinUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import kz.nextstep.tazalykpartners.ui.pinlist.PinViewModel
import kz.nextstep.tazalykpartners.utils.TakeType
import kz.nextstep.tazalykpartners.utils.WorkTime
import rx.Subscriber
import java.time.DayOfWeek
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PinDetailedInfoViewModel : BaseViewModel() {

    private val TAG = PinDetailedInfoFragment::class.java.name

    @Inject
    lateinit var getPinUseCase: GetPinUseCase

    val pinImageSliderAdapter = PinImageSliderAdapter()
    val pinTakeTypeAdapter = PinTakeTypeAdapter()
    val pinWorkTimeAdapter = PinWorkTimeAdapter()

    val pinViewModel = PinViewModel()

    val pinImageAdapterLiveData = MutableLiveData<Boolean>()
    private var currentDayOfWeek = 0
    private var isPointOpen = false

    private val textHashMap = MutableLiveData<HashMap<String, Boolean>>()

    fun getPinById(pinId: String, wasteIdArray: Array<String>) {
        getPinUseCase.execute(object : Subscriber<HashMap<String, Pin>>() {
            override fun onNext(t: HashMap<String, Pin>?) {
                for (key in t?.keys!!) {
                    val pin = t[key]!!
                    pinViewModel.bind(pin, pinId)
                    onGetImageUrlList(pin.imageLink)
                    onGetWasteTypeList(pin.wasteId, wasteIdArray)
                    onGetWorkingScheduleList(pin.workTime)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Log.e(TAG, e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }

    private fun onGetWorkingScheduleList(workTime: String?) {
        currentDayOfWeek = onGetDayOfWeek()
        val workTimeList: MutableList<WorkTime>? = onGetWorkTimeData(workTime!!)
        if (!workTimeList.isNullOrEmpty()) {
            pinWorkTimeAdapter.updateWorkTimeList(workTimeList, currentDayOfWeek, isPointOpen)
            if (currentDayOfWeek < workTimeList.size) {
                val workTimeData = workTimeList[currentDayOfWeek]
                val todayText = "Сегодня "
                val workTimeScheduleText = if (workTimeData.workingTime == "--") todayText + "выходной" else todayText + workTimeData.workingTime
                val workTimeScheduleHashMap: HashMap<String, Boolean> = HashMap()
                workTimeScheduleHashMap[workTimeScheduleText] = isPointOpen
                textHashMap.value = workTimeScheduleHashMap
            }
        }

    }

    fun getTextHashMap() = textHashMap

    private fun onGetDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }
    }

    private fun onGetWorkTimeData(workTime: String): MutableList<WorkTime>? {
        val workTimeDataList = ArrayList<WorkTime>()
        var dayOfTheWeek = 0
        var workingTime = "--"
        var lunchTime = "--"

        if (workTime.contains(";")) {
            val workTimeAllArr = workTime.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (workTimeAllItem in workTimeAllArr) {
                if (!workTimeAllItem.contains("-")) {
                    workingTime = "--"
                    lunchTime = "--"
                    if (dayOfTheWeek == currentDayOfWeek)
                        isPointOpen = false
                } else {
                    if (!workTimeAllItem.contains(",")) {
                        val workTimeAllItemArr =
                            workTimeAllItem.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (workTimeAllItemArr.size >= 4) {
                            workingTime =
                                workTimeAllItemArr[1] + ":" + workTimeAllItemArr[2] + ":" + workTimeAllItemArr[3]
                            workingTime = workingTime.replace(" ".toRegex(), "")
                            lunchTime = "--"
                            if (dayOfTheWeek == currentDayOfWeek)
                                isPointOpen = onCheckTime(workingTime)
                        }
                    } else {
                        val workTimeAllItemArr =
                            workTimeAllItem.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (workTimeAllItemArr.size >= 2) {
                            val workTimeAllItemSplitArrOne =
                                workTimeAllItemArr[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            val workTimeAllItemSplitArrTwo =
                                workTimeAllItemArr[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (workTimeAllItemSplitArrOne.size >= 4 && workTimeAllItemSplitArrTwo.size >= 3) {
                                val timeOne =
                                    workTimeAllItemSplitArrOne[1] + ":" + workTimeAllItemSplitArrOne[2] + ":" + workTimeAllItemSplitArrOne[3]
                                val timeTwo =
                                    workTimeAllItemSplitArrTwo[0] + ":" + workTimeAllItemSplitArrTwo[1] + ":" + workTimeAllItemSplitArrTwo[2]
                                val timeOneArr =
                                    timeOne.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                val timeTwoArr =
                                    timeTwo.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                if (timeOneArr.size >= 2 && timeTwoArr.size >= 2) {
                                    workingTime = timeOneArr[0] + "-" + timeTwoArr[1]
                                    workingTime = workingTime.replace(" ".toRegex(), "")
                                    lunchTime = timeOneArr[1] + "-" + timeTwoArr[0]
                                    lunchTime = lunchTime.replace(" ".toRegex(), "")
                                    if (dayOfTheWeek == currentDayOfWeek)
                                        isPointOpen = onCheckTime(workingTime) && !onCheckTime(lunchTime)
                                }

                            }
                        }
                    }
                }
                val workTimeData = WorkTime(dayOfTheWeek, workingTime, lunchTime)
                workTimeDataList.add(workTimeData)
                dayOfTheWeek++
            }
            if (workTimeDataList.isNotEmpty())
                return workTimeDataList
        }
        return null
    }

    private fun onCheckTime(workingTime: String): Boolean {
        val rightNow = Calendar.getInstance()
        val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinuteIn24Format = rightNow.get(Calendar.MINUTE)
        val currentTimeInMillis = onGetTimeInMillis(currentHourIn24Format, currentMinuteIn24Format)
        val workingTimeArr = workingTime.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (workingTimeArr.size >= 2) {
            val timeOne = workingTimeArr[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val timeTwo = workingTimeArr[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (timeOne.size >= 2 && timeTwo.size >= 2) {
                var hourOne = timeOne[0]
                var hourTwo = timeTwo[0]
                var minuteOne = timeOne[1]
                var minuteTwo = timeTwo[1]
                if (hourOne[0] == '0')
                    hourOne = hourOne.replaceFirst("0".toRegex(), "")
                if (hourTwo[0] == '0')
                    hourTwo = hourTwo.replaceFirst("0".toRegex(), "")
                if (minuteOne[0] == '0')
                    minuteOne = minuteOne.replaceFirst("0".toRegex(), "")
                if (minuteTwo[0] == '0')
                    minuteTwo = minuteTwo.replaceFirst("0".toRegex(), "")
                val startTimeInMillis = onGetTimeInMillis(Integer.parseInt(hourOne), Integer.parseInt(minuteOne))
                val endTimeInMillis = onGetTimeInMillis(Integer.parseInt(hourTwo), Integer.parseInt(minuteTwo))
                return currentTimeInMillis in startTimeInMillis..endTimeInMillis
            }
        }
        return false
    }

    private fun onGetTimeInMillis(hour: Int, minute: Int): Int {
        return 60 * 1000 * (60 * hour + minute)
    }

    private fun onGetWasteTypeList(wasteId: String?, wasteIdArray: Array<String>) {
        val wasteTypeList: MutableList<TakeType> = ArrayList()
        if (wasteId?.contains(";")!!) {
            val wasteTypeArr = wasteId.split(";")
            for (wasteTypeItem in wasteTypeArr) {
                if (wasteTypeItem.contains(",")) {
                    val wasTypeItemArr = wasteTypeItem.split(",")
                    for (item in wasteIdArray) {
                        if (wasTypeItemArr.size >= 3 && item.contains(wasTypeItemArr[1])) {
                            val itemArr = item.split(",")
                            if (itemArr.size >= 5) {
                                wasteTypeList.add(TakeType(itemArr[0], itemArr[1], wasTypeItemArr[2], itemArr[3]))
                                break
                            }
                        }
                    }
                }
            }
            pinTakeTypeAdapter.updateList(wasteTypeList)
        }
    }

    private fun onGetImageUrlList(imageLink: String?) {
        val imageList = imageLink?.split(";")?.toMutableList()
        if (imageList != null) {
            pinImageSliderAdapter.updatePinImage(imageList)
            pinImageAdapterLiveData.value = true
        }
    }
}
