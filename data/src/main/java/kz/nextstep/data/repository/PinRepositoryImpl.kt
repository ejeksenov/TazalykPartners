package kz.nextstep.data.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kz.nextstep.data.entity.PinEntity
import kz.nextstep.data.mapper.PinMapper
import kz.nextstep.domain.repository.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions
import java.io.File
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.floor

class PinRepositoryImpl(val pinMapper: PinMapper) : PinRepository {

    companion object {
        var imageUrlListStr = ""
    }

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.pinTree)
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageReference = firebaseStorage.reference


    override fun deletePin(pinId: String): Observable<Boolean> {
        return Observable.create { subscriber ->
            databaseReference.orderByKey().equalTo(pinId).ref.removeValue().addOnCompleteListener {
                subscriber.onNext(it.isSuccessful)
                subscriber.onCompleted()
            }.addOnFailureListener {
                subscriber.onError(FirebaseException(it.message.toString()))

            }
        }
    }


    override fun addPin(pin: Pin): Observable<String> {
        return Observable.create {
            val pinId = databaseReference.push().key
            if (!pinId.isNullOrBlank()) {
                pin.qrCode = "tazalyk_pin,$pinId"
                val randomString = generateRandomString(12)
                val preVerCode = "${pin.qrCode},$randomString"
                pin.verificationCode = hashString("SHA-512", preVerCode)
                databaseReference.child(pinId).setValue(pin)
                    .addOnCompleteListener { it1 ->
                        if (it1.isSuccessful) {
                            it.onNext(pinId)
                        }
                        it.onCompleted()
                    }.addOnFailureListener { it1 ->
                        it.onError(FirebaseException(it1.message.toString()))
                    }
            }
        }
    }

    private fun generateRandomString(length: Int): String {
        var result = ""
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        for (i in 0..length) {
            result += characters[floor(Math.random() * characters.length).toInt()]
        }
        return result
    }

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    override fun updatePinData(pinId: String, pin: Pin): Observable<Boolean> {
        return Observable.create { subscriber ->
            databaseReference.child(pinId).setValue(pin).addOnCompleteListener {
                subscriber.onNext(it.isSuccessful)
                subscriber.onCompleted()
            }.addOnFailureListener {
                subscriber.onError(FirebaseException(it.message.toString()))
            }
        }
    }

    override fun getPinList(pinIds: String, filterTypes: String): Observable<HashMap<String, Pin>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pinHashMap: HashMap<String, Pin> = HashMap()
                    for (ds in dataSnapshot.children) {
                        if (pinIds.contains(ds.key!!)) {
                            val pinEntity = ds.getValue(PinEntity::class.java)
                            val pin = pinMapper.map(pinEntity!!)
                            if (filterTypes != "") {
                                val pinTypes = pinEntity.wasteId
                                val pinFilterTypeArray = filterTypes.split(";")
                                for (pinTypeItem in pinFilterTypeArray) {
                                    val pinTypeItemArray = pinTypeItem.split(",")
                                    var isMarkingContains = false
                                    if (pinTypeItemArray.size >= 3) {
                                        if (pinTypeItemArray[2].contains(".")) {
                                            for (pinFilterTypeMarking in pinTypeItemArray[2].split(".")) {
                                                if (pinTypes?.contains(pinFilterTypeMarking)!!) {
                                                    pinHashMap[ds.key!!] = pin
                                                    isMarkingContains = true
                                                    break
                                                }
                                            }
                                            if (isMarkingContains)
                                                break
                                        } else if (pinTypes?.contains(pinTypeItem)!!) {
                                            pinHashMap[ds.key!!] = pin
                                            break
                                        }
                                    }

                                }
                            } else {
                                pinHashMap[ds.key!!] = pin
                            }
                        }
                    }
                    it.onNext(pinHashMap)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }
            }
            databaseReference.addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }


    override fun getPinById(pinId: String): Observable<HashMap<String, Pin>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pinHashMap: HashMap<String, Pin> = HashMap()
                    for (ds in dataSnapshot.children) {
                        if (ds != null) {
                            val pinEntity = ds.getValue(PinEntity::class.java)
                            pinHashMap[ds.key!!] = pinMapper.map(pinEntity!!)
                            it.onNext(pinHashMap)
                        }
                    }
                }

            }
            databaseReference.orderByKey().equalTo(pinId).addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }

    override fun deletePinImages(imageUrl: String): Observable<Boolean> {
        return Observable.create { subscription ->
            if (imageUrl.isNotBlank()) {
                val imageReference = firebaseStorage.getReferenceFromUrl(imageUrl)
                imageReference.delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        subscription.onNext(true)
                        subscription.onCompleted()
                    } else
                        subscription.onError(it.exception)


                }
            }
        }
    }

    override fun uploadPinImages(hashMap: HashMap<String, String>, pinId: String): Observable<String> {
        return Observable.create { subscription ->
            imageUrlListStr = ""
            for (imageType in hashMap.keys) {
                val anyKey = databaseReference.push().key
                var imageRef: StorageReference? = null
                if (imageType.isNotBlank())
                    imageRef = storageReference.child("path/${pinId}_$imageType$anyKey")
                if (imageRef != null) {
                    val imageUri = Uri.fromFile(File(hashMap[imageType]!!))
                    val uploadTask = imageRef.putFile(imageUri)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw Objects.requireNonNull<Exception>(task.exception)
                        }
                        imageRef.downloadUrl
                    }.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val uploadImageUri = it.result
                            subscription.onNext("$uploadImageUri")
                            subscription.onCompleted()
                        } else
                            subscription.onError(it.exception)
                    }
                }


            }

        }
    }

}