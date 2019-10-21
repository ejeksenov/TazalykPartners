package kz.nextstep.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import kz.nextstep.data.entity.MarkingEntity
import kz.nextstep.data.mapper.MarkingMapper
import kz.nextstep.domain.model.Marking
import kz.nextstep.domain.repository.MarkingRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions

class MarkingRepositoryImpl(val markingMapper: MarkingMapper): MarkingRepository {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.markingsTree)

    override fun getMarkingListByType(
        wasteType: String,
        wasteTypeMarking: String
    ): Observable<HashMap<String, Marking>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val markingHashMap: HashMap<String, Marking> = HashMap()
                    for (ds in dataSnapshot.children) {
                        val markingEntity = ds.getValue(MarkingEntity::class.java)
                        if (markingEntity != null) {
                            if ((wasteTypeMarking == "all" && markingEntity.markingType?.contains(wasteType)!!) || wasteTypeMarking.contains(markingEntity.markingName!!))  {
                                val marking = markingMapper.map(markingEntity)
                                markingHashMap[ds.key!!] = marking
                            }
                        }
                    }
                    it.onNext(markingHashMap)
                }

            }
            databaseReference.addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }

    }
}