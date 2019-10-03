package kz.nextstep.domain.repository

import kz.nextstep.domain.model.User
import rx.Observable

interface UserRepository {
    fun getUserById(userId: String): Observable<HashMap<String,User>>
    fun getUserListByIds(userIds: String): Observable<HashMap<String,User>>
}