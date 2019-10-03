package kz.nextstep.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import kz.nextstep.data.entity.UserEntity
import kz.nextstep.data.mapper.UserMapper
import kz.nextstep.domain.model.User
import kz.nextstep.domain.repository.UserRepository
import kz.nextstep.domain.utils.AppConstants
import rx.Observable
import rx.subscriptions.Subscriptions

class UserRepositoryImpl(val userMapper: UserMapper) : UserRepository {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(AppConstants.userTree)

    override fun getUserById(userId: String): Observable<User> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        if (ds != null) {
                            val userEntity = ds.getValue(UserEntity::class.java)
                            it.onNext(userMapper.map(userEntity!!))
                        }
                    }
                }

            }
            databaseReference.orderByKey().equalTo(userId).addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }

    override fun getUserListByIds(userIds: String): Observable<List<User>> {
        return Observable.create {
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.onError(FirebaseException(databaseError.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userList: ArrayList<User>? = ArrayList()
                    for (ds in dataSnapshot.children) {
                        if (ds != null && userIds.contains(ds.key.toString())) {
                            val userEntity = ds.getValue(UserEntity::class.java)
                            userList?.add(userMapper.map(userEntity!!))
                        }
                    }
                    it.onNext(userList)
                }

            }

            databaseReference.addValueEventListener(valueEventListener)

            it.add(Subscriptions.create {
                databaseReference.removeEventListener(valueEventListener)
            })
        }
    }

}