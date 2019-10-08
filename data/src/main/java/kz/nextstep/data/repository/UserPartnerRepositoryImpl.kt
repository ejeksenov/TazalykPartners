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
        return Observable.create { subscription ->
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnFailureListener {
                            if (it.message?.toLowerCase()?.contains("password")!!)
                                subscription.onError(FirebaseException("Неверный пароль. Попробуйте заново"))
                            else
                                subscription.onError(FirebaseException(it.message!!))
                        }.addOnCompleteListener {
                            subscription.onNext(it.isSuccessful)
                            subscription.onCompleted()
                        }
                    } else
                        subscription.onError(FirebaseException("Пользователь не зарегистрирован"))
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