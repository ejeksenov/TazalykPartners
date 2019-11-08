package kz.nextstep.tazalykpartners.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kz.nextstep.data.FirebaseHelper
import kz.nextstep.data.mapper.*
import kz.nextstep.data.repository.*
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.*
import kz.nextstep.domain.repository.*
import rx.Observable
import java.lang.RuntimeException
import javax.inject.Singleton

@Module
class DataModule(private val mainApplication: Application) {

    val errorFirebase = "Firebase not supported!"

    @Provides
    fun provideApplication() = mainApplication

    @Provides
    fun provideHistoryPinMapper(): HistoryPinMapper {
        return HistoryPinMapper
    }

    @Provides
    fun provideMarkingMapper(): MarkingMapper {
        return MarkingMapper
    }

    @Provides
    fun providePinMapper(): PinMapper {
        return PinMapper
    }

    @Provides
    fun provideUserMapper(): UserMapper {
        return UserMapper
    }

    @Provides
    fun provideUserPartnerMapper(): UserPartnerMapper {
        return UserPartnerMapper
    }

    @Provides
    fun provideRequestMapper(): RequestsMapper {
        return RequestsMapper
    }

    @Provides
    fun provideFirebaseHelper(): FirebaseHelper {
        return FirebaseHelper
    }

    @Singleton
    @Provides
    fun providePinRepositoryImpl(pinMapper: PinMapper, firebaseHelper: FirebaseHelper, mainApplication: Application) : PinRepository {

        if (firebaseHelper.playServiceStatus(mainApplication))
            return PinRepositoryImpl(pinMapper)
        else {
            return object : PinRepository {
                override fun getPinList(pinIds: String, filterTypes: String): Observable<HashMap<String, Pin>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun getPinById(pinId: String): Observable<HashMap<String,Pin>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun addPin(pin: Pin): Observable<String> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun deletePin(pinId: String): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun updatePinData(pinId: String, pin: Pin): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

            }
        }
    }

    @Singleton
    @Provides
    fun provideUserRepositoryImpl(userMapper: UserMapper, firebaseHelper: FirebaseHelper, mainApplication: Application): UserRepository {
        if (firebaseHelper.playServiceStatus(mainApplication))
            return UserRepositoryImpl(userMapper)
        else {
            return object : UserRepository {
                override fun getUserById(userId: String): Observable<HashMap<String,User>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun getUserListByIds(userIds: String): Observable<HashMap<String,User>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

            }
        }
    }

    @Singleton
    @Provides
    fun provideUserPartnerRepositoryImpl(userPartnerMapper: UserPartnerMapper, firebaseHelper: FirebaseHelper, mainApplication: Application): UserPartnerRepository {
        if (firebaseHelper.playServiceStatus(mainApplication))
            return UserPartnerRepositoryImpl(userPartnerMapper)
        else {
            return object : UserPartnerRepository {
                override fun changeUserPartnerPinId(pinId: String): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun changeUserPartnerData(imageUrl: String?, fullName: String?): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun changePassword(password: String, newPassword: String): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun changeEmail(password: String, newEmail: String): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun sendResetPassword(email: String): Observable<Boolean> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun signOut(): Boolean {
                    return false
                }

                override fun getUserPartnerById(userPartnerId: String): Observable<UserPartner> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun signInWithEmailAndPassword(email: String, password: String): Observable<String> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun getCurrentUserPartner(): Boolean {
                    return false
                }

                override fun getCurrentUserPartnerId(): String {
                    return ""
                }

            }
        }
    }


    @Singleton
    @Provides
    fun provideRequestsRepositoryImpl(requestsMapper: RequestsMapper, firebaseHelper: FirebaseHelper, mainApplication: Application): RequestRepository {
        if (firebaseHelper.playServiceStatus(mainApplication))
            return RequestsRepositoryImpl(requestsMapper)
        else {
            return object : RequestRepository {
                override fun getRequestsByPinId(pinId: String): Observable<HashMap<String, Requests>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

            }
        }
    }

    @Singleton
    @Provides
    fun provideMarkingMapperImpl(markingMapper: MarkingMapper, firebaseHelper: FirebaseHelper, mainApplication: Application): MarkingRepository {
        if (firebaseHelper.playServiceStatus(mainApplication))
            return MarkingRepositoryImpl(markingMapper)
        else {
            return object : MarkingRepository {
                override fun getMarkingListByType(
                    wasteType: String,
                    wasteTypeMarking: String
                ): Observable<HashMap<String, Marking>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

            }
        }
    }

    @Singleton
    @Provides
    fun provideHistoryPinImpl(historyPinMapper: HistoryPinMapper, firebaseHelper: FirebaseHelper, mainApplication: Application): HistoryPinRepository {
        if (firebaseHelper.playServiceStatus(mainApplication))
            return HistoryPinImpl(historyPinMapper)
        else {
            return object : HistoryPinRepository {

                override fun getHistoryPinList(pinId: String): Observable<HashMap<String, HistoryPin>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

            }
        }
    }
}