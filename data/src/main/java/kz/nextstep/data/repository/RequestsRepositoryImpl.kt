package kz.nextstep.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import kz.nextstep.data.entity.RequestsEntity
import kz.nextstep.data.mapper.RequestsMapper
import kz.nextstep.domain.model.Requests
import kz.nextstep.domain.repository.RequestRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions

class RequestsRepositoryImpl(val requestsMapper: RequestsMapper): RequestRepository{

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.requestsTree)

    override fun getRequestsByPinId(pinId: String): Observable<HashMap<String,Requests>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val requestHashMap: HashMap<String, Requests> = HashMap()
                    for (ds in dataSnapshot.children) {
                        if (ds != null) {
                            val requestsEntity = ds.getValue(RequestsEntity::class.java)
                            val requests = requestsMapper.map(requestsEntity!!)
                            if (pinId.contains(requests.pin_id!!)) {
                                requestHashMap[ds.key!!] = requests
                            }
                        }
                    }
                    it.onNext(requestHashMap)
                }

            }

            databaseReference.addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }
}