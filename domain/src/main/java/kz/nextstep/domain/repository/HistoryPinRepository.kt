package kz.nextstep.domain.repository

import kz.nextstep.domain.model.HistoryPin
import rx.Observable
import kotlin.collections.HashMap

interface HistoryPinRepository {
    fun getHistoryPinList(pinId: String): Observable<HashMap<String, HistoryPin>>
    fun addHistoryPin(historyPin: HistoryPin): Observable<Boolean>
}