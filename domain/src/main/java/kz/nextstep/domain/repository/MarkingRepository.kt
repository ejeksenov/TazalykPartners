package kz.nextstep.domain.repository

import kz.nextstep.domain.model.Marking
import rx.Observable

interface MarkingRepository {
    fun getMarkingListByType(wasteType: String, wasteTypeMarking: String): Observable<HashMap<String, Marking>>
}