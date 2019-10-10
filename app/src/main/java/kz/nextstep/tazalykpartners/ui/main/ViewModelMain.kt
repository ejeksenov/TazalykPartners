package kz.nextstep.tazalykpartners.ui.main

import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.UserPartner

class ViewModelMain {

    private val email = MutableLiveData<String>()
    private val name = MutableLiveData<String>()
    private val pinIds = MutableLiveData<String>()
    private val imageUrl = MutableLiveData<String>()

    fun bind(userPartner: UserPartner) {
        email.value = userPartner.email
        name.value = userPartner.name
        pinIds.value = userPartner.pinIds
        imageUrl.value = userPartner.imageUrl
    }

    fun getEmail() = email
    fun getName() = name
    fun getPinIds() = pinIds
    fun getImageUrl() = imageUrl
}