package kz.nextstep.domain.repository

import kz.nextstep.domain.model.Requests
import rx.Observable

interface RequestRepository {
    fun getRequestsByPinId(pinId: String): Observable<HashMap<String,Requests>>
}