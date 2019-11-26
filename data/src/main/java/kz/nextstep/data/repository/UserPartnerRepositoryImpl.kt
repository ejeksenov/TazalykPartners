package kz.nextstep.data.repository

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import kz.nextstep.data.entity.UserPartnerEntity
import kz.nextstep.data.mapper.UserPartnerMapper
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.repository.UserPartnerRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import java.util.*

class UserPartnerRepositoryImpl(val userPartnerMapper: UserPartnerMapper) : UserPartnerRepository {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.userPartnerTree)

    override fun changeUserPartnerPinId(pinId: String): Observable<Boolean> {
        return Observable.create { subscription ->
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val userPartnerId = currentUser.uid
                val valueEventListener = object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                        subscription.onError(FirebaseException(databaseError.message))
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val userPartner = ds.getValue(UserPartner::class.java)
                            if (userPartner != null) {
                                var pinIds = userPartner.pinIds
                                if (!pinIds.isNullOrBlank() && pinIds.contains(pinId)) {
                                    pinIds += ",$pinId"
                                } else pinIds = pinId

                                databaseReference.child(userPartnerId).child("pinIds").setValue(pinIds)
                                    .addOnCompleteListener {
                                        subscription.onNext(it.isSuccessful)
                                        subscription.onCompleted()
                                    }.addOnFailureListener {
                                        subscription.onError(FirebaseException(it.message!!))
                                    }
                                break
                            }
                        }
                    }

                }

                databaseReference.orderByKey().equalTo(userPartnerId).addValueEventListener(valueEventListener)

                subscription.add(Subscriptions.create {
                    databaseReference.removeEventListener(valueEventListener)
                })
            }
        }
    }

    override fun changeUserPartnerData(imageUrl: String?, fullName: String?): Observable<Boolean> {
        return Observable.create { subscription ->
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (!userId.isNullOrBlank()) {
                if (!imageUrl.isNullOrBlank()) {
                    val imageUri = Uri.parse(imageUrl)
                    val storageRef = FirebaseStorage.getInstance().reference
                    val profileImageRef = storageRef.child("avatar/$userId/1.jpg")
                    val uploadTask = profileImageRef.putFile(imageUri)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw Objects.requireNonNull<Exception>(task.exception)
                        }
                        profileImageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uri = task.result
                            databaseReference.child(userId).child("imageUrl").setValue(uri.toString())
                                .addOnCompleteListener {
                                    if (task.isSuccessful && !fullName.isNullOrBlank()) {
                                        onChangeUserPartnerName(subscription, userId, fullName)
                                    }
                                }.addOnFailureListener {
                                    subscription.onError(it)
                                }
                        } else if (task.exception != null) {
                            subscription.onError(task.exception)
                        }
                    }
                } else if (!fullName.isNullOrBlank())
                    onChangeUserPartnerName(subscription, userId, fullName)
            }
        }
    }

    private fun onChangeUserPartnerName(
        subscription: Subscriber<in Boolean>,
        userId: String,
        fullName: String
    ) {
        databaseReference.child(userId).child("name").setValue(fullName)
            .addOnCompleteListener { it1 ->
                subscription.onNext(it1.isSuccessful)
                subscription.onCompleted()
            }.addOnFailureListener { it1 ->
                subscription.onError(it1)
            }
    }

    override fun changePassword(password: String, newPassword: String): Observable<Boolean> {
        return Observable.create { subscription ->
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userEmail = currentUser?.email
            val authCredential: AuthCredential = EmailAuthProvider.getCredential(userEmail!!, password)
            currentUser.reauthenticate(authCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener { it1 ->
                        subscription.onNext(it1.isSuccessful)
                        subscription.onCompleted()
                    }.addOnFailureListener { it1 ->
                        subscription.onError(FirebaseException(it1.message!!))
                    }
                } else if (it.exception != null) {
                    subscription.onError(FirebaseException(it.exception?.message!!))
                }
            }.addOnFailureListener {
                subscription.onError(FirebaseException(it.message!!))
            }
        }
    }

    override fun changeEmail(password: String, newEmail: String): Observable<Boolean> {
        return Observable.create { subscription ->
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userEmail = currentUser?.email
            val authCredential: AuthCredential = EmailAuthProvider.getCredential(userEmail!!, password)
            currentUser.reauthenticate(authCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    currentUser.updateEmail(newEmail).addOnCompleteListener { it1 ->
                        if (it1.isSuccessful) {
                            databaseReference.child(currentUser.uid).child("email").setValue(newEmail)
                                .addOnCompleteListener { it2 ->
                                    if (it2.isSuccessful)
                                        currentUser.sendEmailVerification()
                                    subscription.onNext(it2.isSuccessful)
                                    subscription.onCompleted()
                                }.addOnFailureListener { it2 ->
                                    subscription.onError(FirebaseException(it2.message!!))
                                }
                        }

                    }.addOnFailureListener { it1 ->
                        subscription.onError(FirebaseException(it1.message!!))
                    }
                } else if (it.exception != null) {
                    subscription.onError(FirebaseException(it.exception?.message!!))
                }
            }.addOnFailureListener {
                subscription.onError(FirebaseException(it.message!!))
            }
        }
    }

    override fun sendResetPassword(email: String): Observable<Boolean> {
        return Observable.create { subscription ->
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                subscription.onNext(it.isSuccessful)
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
                        var adminId = ""
                        for (ds in dataSnapshot.children) {
                            val userPartnerEntity = ds.getValue(UserPartnerEntity::class.java)
                            val pinIds = userPartnerEntity?.pinIds
                            val productIds = userPartnerEntity?.productIds
                            if (pinIds == "" && productIds == "")
                                subscription.onError(FirebaseException(AppConstants.ERROR_USER_NOT_FOUND))
                            else if (pinIds?.contains(",")!!)
                                responseString = AppConstants.SUCCESS_PIN_DIRECTOR
                            else if (pinIds != "" && !pinIds.contains(",")) {
                                adminId = pinIds
                                responseString = AppConstants.SUCCESS_PIN_ADMIN
                            } else if (productIds != "")
                                responseString = AppConstants.SUCCESS_PRODUCT_SPONSOR
                        }
                        val mAuth = FirebaseAuth.getInstance()
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnFailureListener {
                            if (it.message?.toLowerCase()?.contains("password")!!)
                                subscription.onError(FirebaseException(AppConstants.ERROR_INVALID_PASSWORD))
                            else
                                subscription.onError(FirebaseException(it.message!!))
                        }.addOnCompleteListener {
                            if (mAuth.currentUser?.isEmailVerified!!) {
                                if (responseString == AppConstants.SUCCESS_PIN_ADMIN)
                                    onSaveNewToken(adminId)
                                subscription.onNext(responseString)
                                subscription.onCompleted()
                            } else {
                                mAuth.signOut()
                                subscription.onError(FirebaseException(AppConstants.ERROR_VERIFY_EMAIL))
                            }
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

    private fun onSaveNewToken(adminId: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val token = it.result?.token
                    if (!token.isNullOrBlank())
                    FirebaseDatabase.getInstance().reference.child(AppConstants.messagingTree).child(adminId).setValue(token)
                }
            }
    }
}