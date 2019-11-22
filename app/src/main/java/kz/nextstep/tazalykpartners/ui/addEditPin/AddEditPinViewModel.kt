package kz.nextstep.tazalykpartners.ui.addEditPin

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.usecase.partner.ChangeUserPartnerPinIdUseCase
import kz.nextstep.domain.usecase.pin.*
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import javax.inject.Inject

class AddEditPinViewModel : BaseViewModel() {

    @Inject
    lateinit var getPinUseCase: GetPinUseCase

    @Inject
    lateinit var addPinUseCase: AddPinUseCase

    @Inject
    lateinit var updatePinDataUseCase: UpdatePinDataUseCase

    @Inject
    lateinit var changeUserPartnerPinIdUseCase: ChangeUserPartnerPinIdUseCase

    @Inject
    lateinit var deletePinImagesUseCase: DeletePinImagesUseCase

    @Inject
    lateinit var uploadPinImagesUseCase: UploadPinImagesUseCase


    val pinMutableLiveData = MutableLiveData<Pin>()
    val newPinMutableLiveData = MutableLiveData<Boolean>()
    val deletePinImagesMutableLiveData = MutableLiveData<Boolean>()
    val uploadPinImagesMutableLiveData = MutableLiveData<String>()
    val uploadPinImagesDoneMutableLiveData = MutableLiveData<Boolean>()

    fun getPinData(pinId: String) {
        getPinUseCase.execute(object : Subscriber<HashMap<String, Pin>>() {
            override fun onNext(t: HashMap<String, Pin>?) {
                if (!t.isNullOrEmpty()) {
                    for (key in t.keys) {
                        pinMutableLiveData.value = t[key]
                    }
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }


    fun addNewPin(pin: Pin) {
        addPinUseCase.execute(object : Subscriber<String>() {
            override fun onNext(pinId: String?) {
                if (!pinId.isNullOrBlank()) {
                    onAddNewPinId(pinId)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, AppConstants.emptyParam, pin)
    }

    private fun onAddNewPinId(pinId: String) {
        changeUserPartnerPinIdUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                newPinMutableLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, AppConstants.emptyParam)
    }


    fun updatePinData(pinId: String, pin: Pin) {
        updatePinDataUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean?) {
                newPinMutableLiveData.value = t
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                showToastMessage(e?.message)
            }

        }, pinId, pin)
    }

    fun deletePinImages(deletedPinLogoUrl: String, deletedPinImagesUrlList: MutableList<String>) {
        if (deletedPinLogoUrl.isNotBlank())
            deletedPinImagesUrlList.add(deletedPinLogoUrl)
        if (deletedPinImagesUrlList.isNotEmpty()) {
            var counter = 0
            for (item in deletedPinImagesUrlList) {
                deletePinImagesUseCase.execute(object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (t!!)
                            counter++
                        if (counter == deletedPinImagesUrlList.size)
                            deletePinImagesMutableLiveData.value = t
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        showToastMessage(e?.message)
                        Log.e("PinDataDelete", e?.message)
                    }

                }, item, AppConstants.emptyParam)
            }
        } else
            deletePinImagesMutableLiveData.value = true
    }


    fun uploadPinImages(uploadPinLogoUri: Uri?, uploadPinImagesUriList: MutableList<Uri>, pinId: String) {
        val uploadImagesHashMap: HashMap<String, String> = HashMap()
        if (uploadPinLogoUri != null)
            uploadImagesHashMap[AppConstants.PICTURE_TYPE_LOGO] = uploadPinLogoUri.toString()
        if (uploadPinImagesUriList.isNotEmpty()) {
            for ((index, item) in uploadPinImagesUriList.withIndex()) {
                val imageTypeKey = "${AppConstants.PICTURE_TYPE_IMAGE}${index + 1}"
                uploadImagesHashMap[imageTypeKey] = item.toString()
            }
        }
        var counter = 0
        if (uploadImagesHashMap.isNotEmpty()) {
            for ((key, item) in uploadImagesHashMap) {
                val uploadOneImageHashMap: HashMap<String, String> = HashMap()
                uploadOneImageHashMap[key] = item
                uploadPinImagesUseCase.execute(object : Subscriber<String>() {
                    override fun onNext(t: String?) {
                        counter++
                        uploadPinImagesMutableLiveData.value = t
                        if (counter == uploadImagesHashMap.size)
                            uploadPinImagesDoneMutableLiveData.value = true
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        showToastMessage(e?.message)
                        Log.e("PinDataUpload", e?.message)
                    }

                }, uploadOneImageHashMap, pinId)
            }
        } else {
            uploadPinImagesMutableLiveData.value = ""
            uploadPinImagesDoneMutableLiveData.value = true
        }

    }

    override fun onCleared() {
        super.onCleared()
        getPinUseCase.unsubscribe()
        addPinUseCase.unsubscribe()
        updatePinDataUseCase.unsubscribe()
        changeUserPartnerPinIdUseCase.unsubscribe()
        deletePinImagesUseCase.unsubscribe()
        uploadPinImagesUseCase.unsubscribe()
    }
}