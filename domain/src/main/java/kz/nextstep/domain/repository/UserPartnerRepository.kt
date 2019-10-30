package kz.nextstep.domain.repository

import kz.nextstep.domain.model.UserPartner
import rx.Observable

interface UserPartnerRepository {
    fun getUserPartnerById(userPartnerId: String): Observable<UserPartner>
    fun signInWithEmailAndPassword(email: String, password: String): Observable<String>
    fun getCurrentUserPartner(): Boolean
    fun getCurrentUserPartnerId(): String
    fun signOut(): Boolean
    fun sendResetPassword(email: String) : Observable<Boolean>
    fun changeUserPartnerData(imageUrl: String?, fullName: String?): Observable<Boolean>
    fun changePassword(password: String, newPassword: String): Observable<Boolean>
    fun changeEmail(password: String, newEmail: String): Observable<Boolean>
}