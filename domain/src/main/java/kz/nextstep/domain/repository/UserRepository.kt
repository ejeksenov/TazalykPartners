package kz.nextstep.domain.repository

import kz.nextstep.domain.model.User
import rx.Observable

interface UserRepository {
    fun getUserById(userId: String): Observable<User>
    fun getUserListByIds(userIds: String): Observable<List<User>>
}