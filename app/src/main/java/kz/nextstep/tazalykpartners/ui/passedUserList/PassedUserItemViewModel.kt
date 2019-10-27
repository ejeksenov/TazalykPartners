package kz.nextstep.tazalykpartners.ui.passedUserList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.utils.data.PassedUserItem

class PassedUserItemViewModel: ViewModel() {
    private val userName = MutableLiveData<String>()
    private val userImageUrl = MutableLiveData<String>()
    private val passedTotal = MutableLiveData<String>()
    private val passedDate = MutableLiveData<String>()

    fun bind(passedUserItem: PassedUserItem) {
        userName.value = passedUserItem.userName
        userImageUrl.value = passedUserItem.userImageUrl
        passedTotal.value = "${passedUserItem.passedTotal} кг"
        passedDate.value = passedUserItem.passedDate
    }

    fun getUserName() = userName
    fun getUserImageUrl() = userImageUrl
    fun getPassedTotal() = passedTotal
    fun getPassedDate() = passedDate
}