package kz.nextstep.tazalykpartners.ui.passedUserList

import android.widget.Toast
import kz.nextstep.domain.model.User
import kz.nextstep.domain.usecase.user.GetUserByIdUseCase
import kz.nextstep.domain.usecase.user.GetUserListByIdsUseCase
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.base.BaseViewModel
import kz.nextstep.tazalykpartners.utils.data.PassedUserItem
import kz.nextstep.tazalykpartners.utils.data.WasteItem
import rx.Subscriber
import javax.inject.Inject

class StatisticsPassedUserListViewModel : BaseViewModel() {

    @Inject
    lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @Inject
    lateinit var getUserListByIdsUseCase: GetUserListByIdsUseCase

    val passedUserListAdapter = PassedUserListAdapter()

    fun getPassedUserListData(dataList: MutableList<WasteItem>) {

        if (!dataList.isNullOrEmpty()) {
            val passedUserList: MutableList<PassedUserItem> = ArrayList()
            //val userListHashMap: HashMap<String, User> = HashMap()
            var userIds = ""
            for (item in dataList) {
                userIds += item.userId + ","
            }
            getUserListByIdsUseCase.execute(object : Subscriber<HashMap<String, User>>() {
                override fun onNext(t: HashMap<String, User>?) {
                    if (!t.isNullOrEmpty()) {
                        for (item in dataList) {
                            if (t.containsKey(item.userId)) {
                                val user = t[item.userId]
                                if (user != null) {
                                    passedUserList.add(
                                        PassedUserItem(
                                            user.userName,
                                            user.imageLink,
                                            item.passed_total,
                                            item.passedDate
                                        )
                                    )
                                }
                            }
                        }
                        if (!passedUserList.isNullOrEmpty())
                            passedUserListAdapter.updatePassedUserItemList(passedUserList)
                    }
                }

                override fun onCompleted() {}

                override fun onError(e: Throwable?) {
                    Toast.makeText(
                        MainApplication.INSTANCE?.applicationContext,
                        e?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }, userIds, AppConstants.emptyParam)
        }

    }
}