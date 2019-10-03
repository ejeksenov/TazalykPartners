package kz.nextstep.tazalykpartners.di

import dagger.Module
import dagger.Provides
import kz.nextstep.data.FirebaseHelper
import kz.nextstep.data.mapper.PinMapper
import kz.nextstep.data.repository.PinRepositoryImpl
import kz.nextstep.domain.PinRepository
import kz.nextstep.domain.model.Pin
import kz.nextstep.tazalykpartners.MainApplication
import rx.Observable
import java.lang.RuntimeException
import javax.inject.Singleton

@Module
class DataModule(private val mainApplication: MainApplication) {

    @Provides
    fun provideApplication() = mainApplication

    @Provides
    fun providePinMapper(): PinMapper {
        return PinMapper
    }

    @Provides
    fun provideFirebaseHelper(): FirebaseHelper {
        return FirebaseHelper
    }

    @Singleton
    @Provides
    fun providePinRepositoryImpl(pinMapper: PinMapper, firebaseHelper: FirebaseHelper, mainApplication: MainApplication) : PinRepository {
        val errorFirebase = "Проблема с базой!"
        if (firebaseHelper.playServiceStatus(mainApplication))
            return PinRepositoryImpl(pinMapper)
        else {
            return object : PinRepository {
                override fun getPinList(pinIds: String): Observable<List<Pin>> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun getPinById(pinId: String): Observable<Pin> {
                    return Observable.error(RuntimeException(errorFirebase))
                }

                override fun addPin(pin: Pin): Observable<Boolean> {
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
}