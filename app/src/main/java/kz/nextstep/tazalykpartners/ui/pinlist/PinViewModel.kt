package kz.nextstep.tazalykpartners.ui.pinlist

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.model.Requests
import kz.nextstep.domain.usecase.request.GetRequestsByPinIdUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class PinViewModel : BaseViewModel() {

    private val name = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val address = MutableLiveData<String>()
    private val phone = MutableLiveData<String>()
    private val workTime = MutableLiveData<String>()
    private val qrCode = MutableLiveData<String>()
    private val imageLink = MutableLiveData<String>()
    private val phoneName = MutableLiveData<String>()
    private val logo = MutableLiveData<String>()
    private val city = MutableLiveData<String>()
    private val country = MutableLiveData<String>()
    private val wasteId = MutableLiveData<String>()
    private val partner = MutableLiveData<String>()
    private val latlng = MutableLiveData<String>()
    private val verificationCode = MutableLiveData<String>()
    private val averageRating = MutableLiveData<String>()

    @Inject
    lateinit var getRequestsByPinIdUseCase: GetRequestsByPinIdUseCase

    fun bind(pinEntity: Pin, pinId: String) {
        name.value = pinEntity.name
        address.value = pinEntity.address
        description.value = pinEntity.description
        phone.value = pinEntity.phone
        phoneName.value = pinEntity.phoneName
        workTime.value = pinEntity.workTime
        qrCode.value = pinEntity.qrCode
        imageLink.value = pinEntity.imageLink
        logo.value = pinEntity.logo
        city.value = pinEntity.city
        country.value = pinEntity.country
        wasteId.value = pinEntity.wasteId
        partner.value = pinEntity.partner
        latlng.value = pinEntity.latlng
        verificationCode.value = pinEntity.verificationCode
        onGetAverageRating(pinId)
    }

    private fun onGetAverageRating(pinId: String) {
        var pinAverage: Double
        getRequestsByPinIdUseCase.execute(object : Subscriber<HashMap<String, Requests>>() {
            override fun onNext(t: HashMap<String, Requests>?) {
                pinAverage = 0.0
                var cnt = 0
                for (key in t?.keys!!) {
                    val request = t[key]
                    val rating_grade = request?.rating_grade
                    if (rating_grade != null && rating_grade != "") {
                        cnt++
                        pinAverage += rating_grade.toDouble()
                    }
                }

                if (pinAverage != 0.0 && cnt != 0)
                    pinAverage /= cnt

                averageRating.value = pinAverage.toString()
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Toast.makeText(MainApplication.INSTANCE?.applicationContext, e?.message, Toast.LENGTH_SHORT).show()
            }

        }, pinId, AppConstants.emptyParam)
    }

    fun getName() = name
    fun getAddress() = address
    fun getLogo() = logo
    fun getAverageRating() = averageRating
}