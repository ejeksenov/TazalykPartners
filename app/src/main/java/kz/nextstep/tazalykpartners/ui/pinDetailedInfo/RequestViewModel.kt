package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kz.nextstep.domain.model.Requests
import kz.nextstep.domain.model.User
import kz.nextstep.domain.usecase.user.GetUserByIdUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import rx.Subscriber
import java.math.RoundingMode
import javax.inject.Inject

class RequestViewModel: BaseViewModel() {
    private val name = MutableLiveData<String>()
    private val name_of_company = MutableLiveData<String>()
    private val phone_number = MutableLiveData<String>()
    private val short_description = MutableLiveData<String>()
    private val request_type = MutableLiveData<String>()
    private val address_city = MutableLiveData<String>()
    private val waste_type = MutableLiveData<String>()
    private val city = MutableLiveData<String>()
    private val rating_grade = MutableLiveData<String>()
    private val pin_id = MutableLiveData<String>()
    private val user_id = MutableLiveData<String>()
    private val comment_date = MutableLiveData<String>()

    private val userName = MutableLiveData<String>()
    private val userImageUrl = MutableLiveData<String>()

    @Inject
    lateinit var getUserByIdUseCase: GetUserByIdUseCase

    fun bind(requestsEntity: Requests) {
        name.value = requestsEntity.name
        name_of_company.value = requestsEntity.name_of_company
        phone_number.value = requestsEntity.phone_number
        short_description.value = requestsEntity.short_description
        request_type.value = requestsEntity.request_type
        address_city.value = requestsEntity.address_city
        waste_type.value = requestsEntity.waste_type
        city.value = requestsEntity.city
        rating_grade.value = getRatingGrade(requestsEntity.rating_grade)
        pin_id.value = requestsEntity.pin_id
        user_id.value = requestsEntity.user_id
        comment_date.value = requestsEntity.comment_date
        getUserById(requestsEntity.user_id)
    }

    private fun getRatingGrade(ratingGrade: String?): String? {
        return if(ratingGrade != null && ratingGrade != "" && ratingGrade != "0.0" && ratingGrade != "0"  )
            ratingGrade
        else
            "0"
    }

    private fun getUserById(userId: String?) {
        getUserByIdUseCase.execute(object : Subscriber<HashMap<String, User>>() {
            override fun onNext(t: HashMap<String, User>?) {
                for (key in t?.keys!!) {
                    val userData = t[key]
                    userName.value = userData?.userName
                    userImageUrl.value = userData?.imageLink
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Toast.makeText(MainApplication.INSTANCE?.applicationContext, e?.message, Toast.LENGTH_SHORT).show()
            }

        }, userId!!, AppConstants.emptyParam)
    }

    fun getUserName() = userName
    fun getUserImageUrl() = userImageUrl
    fun getRatingGrade() = rating_grade
    fun getCommentDate() = comment_date
    fun getComment() = short_description

}