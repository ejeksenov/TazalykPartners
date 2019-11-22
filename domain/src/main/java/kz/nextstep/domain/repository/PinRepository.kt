package kz.nextstep.domain.repository

import com.sun.jndi.toolkit.url.Uri
import kz.nextstep.domain.model.Pin
import rx.Observable

interface PinRepository {
    fun getPinList(pinIds: String, filterTypes: String): Observable<HashMap<String, Pin>>
    fun getPinById(pinId: String): Observable<HashMap<String, Pin>>
    fun addPin(pin: Pin): Observable<String>
    fun deletePin(pinId: String): Observable<Boolean>
    fun updatePinData(pinId: String, pin: Pin): Observable<Boolean>
    fun deletePinImages(imageUrl: String): Observable<Boolean>
    fun uploadPinImages(hashMap: HashMap<String, String>, pinId: String): Observable<String>
}