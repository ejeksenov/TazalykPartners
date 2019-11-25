package kz.nextstep.tazalykpartners.ui.addPointsToUser

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.usecase.historyPin.AddHistoryPinUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.ChangeDateFormat
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class AddPointsToUserViewModel: BaseViewModel() {

    @Inject
    lateinit var addHistoryPinUseCase: AddHistoryPinUseCase

    val historyPinLiveData = MutableLiveData<Boolean>()


    fun onSaveToHistoryPin(userId: String, pinId: String, wasteType: String) {
        val currentTime = ChangeDateFormat.getCurrentDate()
        val historyPin = HistoryPin(pinId, userId, currentTime, wasteType)
        addHistoryPinUseCase.execute(object : Subscriber<Boolean>(){
            override fun onNext(t: Boolean?) {
                historyPinLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                historyPinLiveData.value = false
                showToastMessage(e?.message)
            }

        }, historyPin, AppConstants.emptyParam)
    }

    override fun onCleared() {
        super.onCleared()
        addHistoryPinUseCase.unsubscribe()
    }
}