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

class UserPartnerRepositoryImpl(val userPartnerMapper: UserPartnerMapper) : UserPartnerRepository {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.userPartnerTree)

    override fun sendResetPassword(email: String): Observable<Boolean> {
        return Observable.create { subscription ->
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful)
                    subscription.onNext(true)
                else
                    subscription.onNext(false)
                subscription.onCompleted()
            }.addOnFailureListener {
                subscription.onError(FirebaseException(it.message!!))
            }
        }
    }

    override fun signOut(): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            mAuth.signOut()
            return true
        }
        return false
    }

    override fun getCurrentUserPartner(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun getCurrentUserPartnerId(): String {
        val mUser = FirebaseAuth.getInstance().currentUser
        return mUser?.uid ?: ""
    }

    override fun getUserPartnerById(userPartnerId: String): Observable<UserPartner> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        val userPartnerEntity = dataSnapshot.getValue(UserPartnerEntity::class.java)
                        it.onNext(userPartnerMapper.map(userPartnerEntity!!))

                    } else
                        it.onError(FirebaseException(AppConstants.ERROR_USER_NOT_FOUND))
                }

            }

            databaseReference.child(userPartnerId).addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }

    }

    override fun signInWithEmailAndPassword(email: String, password: String): Observable<String> {
        return Observable.create { subscription ->
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var responseString: String? = null
                    if (dataSnapshot.value != null) {
                        for (ds in dataSnapshot.children) {
                            val userPartnerEntity = ds.getValue(UserPartnerEntity::class.java)
                            val pinIds = userPartnerEntity?.pinIds
                            val productIds = userPartnerEntity?.productIds
                            if (pinIds == "" && productIds == "")
                                subscription.onError(FirebaseException(AppConstants.ERROR_USER_NOT_FOUND))
                            else if (pinIds?.contains(",")!!)
                                responseString = AppConstants.SUCCESS_PIN_DIRECTOR
                            else if (pinIds != "" && !pinIds.contains(","))
                                responseString = AppConstants.SUCCESS_PIN_ADMIN
                            else if (productIds != "")
                                responseString = AppConstants.SUCCESS_PRODUCT_SPONSOR
                        }
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnFailureListener {
                            if (it.message?.toLowerCase()?.contains("password")!!)
                                subscription.onError(FirebaseException(AppConstants.ERROR_INVALID_PASSWORD))
                            else
                                subscription.onError(FirebaseException(it.message!!))
                        }.addOnCompleteListener {
                            subscription.onNext(responseString)
                            subscription.onCompleted()
                        }
                    } else
                        subscription.onError(FirebaseException(AppConstants.ERROR_USER_NOT_FOUND))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    subscription.onError(FirebaseException(databaseError.message))
                }

            }
            databaseReference.orderByChild("email").equalTo(email).addValueEventListener(valueEventListener)

            subscription.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }
}