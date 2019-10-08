package kz.nextstep.domain.repository

import kz.nextstep.domain.model.UserPartner
import rx.Observable

interface UserPartnerRepository {
    fun getUserPartnerById(userPartnerId: String): Observable<UserPartner>
    fun signInWithEmailAndPassword(email: String, password: String): Observable<Boolean>
    fun getCurrentUserPartner(): Boolean
    fun getCurrentUserPartnerId(): String
    fun signOut(): Boolean
    fun sendResetPassword(email: String) : Observable<Boolean>
}