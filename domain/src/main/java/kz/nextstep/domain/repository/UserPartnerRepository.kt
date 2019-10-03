package kz.nextstep.domain.repository

import kz.nextstep.domain.model.UserPartner
import rx.Observable

interface UserPartnerRepository {
    fun getUserPartnerById(userPartnerId: String): Observable<UserPartner>
}