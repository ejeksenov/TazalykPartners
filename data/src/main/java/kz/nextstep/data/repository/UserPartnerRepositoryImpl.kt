package kz.nextstep.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kz.nextstep.data.entity.UserPartnerEntity
import kz.nextstep.data.mapper.UserPartnerMapper
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.repository.UserPartnerRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions

class UserPartnerRepositoryImpl(val userPartnerMapper: UserPartnerMapper): UserPartnerRepository {
    override fun getCurrentUserPartner(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun getCurrentUserPartnerId(): String {
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null)
            return mUser.uid
        return ""
    }

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.userPartnerTree)

    override fun getUserPartnerById(userPartnerId: String): Observable<UserPartner> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        if (ds != null) {
                            val userPartnerEntity = ds.getValue(UserPartnerEntity::class.java)
                            it.onNext(userPartnerMapper.map(userPartnerEntity!!))
                        }
                    }
                }

            }

            databaseReference.orderByKey().equalTo(userPartnerId).addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }

    }

    override fun signInWithEmailAndPassword(email: String, password: String): Observable<Boolean> {
        return Observable.create {subscription ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnFailureListener {
                subscription.onError(FirebaseException(it.message.toString()))
            }.addOnCompleteListener {
                subscription.onNext(it.isSuccessful)
                subscription.onCompleted()
            }
        }
    }
}