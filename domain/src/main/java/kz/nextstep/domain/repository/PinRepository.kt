package kz.nextstep.domain

import kz.nextstep.domain.model.Pin
import rx.Observable

interface PinRepository {
    fun getPinList(pinIds: String): Observable<List<Pin>>
    fun getPinById(pinId: String): Observable<Pin>
    fun addPin(pin: Pin): Observable<Boolean>
    fun deletePin(pinId: String): Observable<Boolean>
    fun updatePinData(pinId: String, pin: Pin): Observable<Boolean>
}