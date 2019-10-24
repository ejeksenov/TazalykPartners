package kz.nextstep.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import kz.nextstep.data.entity.HistoryPinEntity
import kz.nextstep.data.mapper.HistoryPinMapper
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.repository.HistoryPinRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions

class HistoryPinImpl(val historyPinMapper: HistoryPinMapper) : HistoryPinRepository {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.historyPinTree)

    override fun getHistoryPinList(pinId: String): Observable<HashMap<String, HistoryPin>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val historyPinHashMap: HashMap<String, HistoryPin> = HashMap()
                    for (ds in dataSnapshot.children) {
                        val historyPinEntity = ds.getValue(HistoryPinEntity::class.java)
                        if (historyPinEntity != null) {
                            val historyPin = historyPinMapper.map(historyPinEntity)
                            if (historyPin.pinId != "" && historyPin.pinId!! in pinId) {
                                historyPinHashMap[ds.key!!] = historyPin
                            }
                        }
                    }
                    it.onNext(historyPinHashMap)
                }

            }
            databaseReference.addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }
}