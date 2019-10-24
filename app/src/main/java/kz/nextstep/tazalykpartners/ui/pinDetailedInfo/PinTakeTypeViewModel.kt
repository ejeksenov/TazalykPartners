package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Marking
import kz.nextstep.domain.usecase.marking.GetMarkingListByTypeUseCase
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import kz.nextstep.tazalykpartners.utils.data.TakeType
import rx.Subscriber
import javax.inject.Inject

class PinTakeTypeViewModel: BaseViewModel() {

    @Inject
    lateinit var getMarkingListByTypeUseCase: GetMarkingListByTypeUseCase

    val pinTakeTypeMarkingAdapter = PinTakeTypeMarkingAdapter()

    private var takeType = MutableLiveData<String>()
    private var takeTypeName = MutableLiveData<String>()
    private var takeTypeMarking = MutableLiveData<String>()
    private var takeTypeLogo = MutableLiveData<String>()

    fun bind(takeTypeItem: TakeType) {
        onGetMarkingImageUrlList(takeTypeItem.takeTypeName, takeTypeItem.takeTypeMarking)
        takeType.value = takeTypeItem.takeType
        takeTypeName.value = takeTypeItem.takeTypeName
        takeTypeLogo.value = takeTypeItem.takeTypeLogo
    }

    private fun onGetMarkingImageUrlList(takeTypeName: String?, takeTypeMarkingStr: String?){
        var imageUrl = ""
        getMarkingListByTypeUseCase.execute(object : Subscriber<HashMap<String, Marking>>() {
            override fun onNext(t: HashMap<String, Marking>?) {
                for (key in t?.keys!!) {
                    imageUrl += t[key]?.markingLogoUrl + ","
                }
                takeTypeMarking.value = imageUrl
                pinTakeTypeMarkingAdapter.updateList(imageUrl)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Toast.makeText(MainApplication.INSTANCE?.applicationContext, e?.message, Toast.LENGTH_LONG).show()
            }

        }, takeTypeName!!, takeTypeMarkingStr!!)
    }

    fun getTakeType() = takeType
    fun getTakeTypeName() = takeTypeName
    fun getTakeMarking() = takeTypeMarking
    fun getTakeTypeLogo() = takeTypeLogo
}