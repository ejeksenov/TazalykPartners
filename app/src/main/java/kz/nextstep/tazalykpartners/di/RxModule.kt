package kz.nextstep.tazalykpartners.di

import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.android.schedulers.AndroidSchedulers
import rx.Scheduler
import rx.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {
    companion object {
        const val MAIN = "main"
        const val IO = "io"
    }


    @Provides
    @Singleton
    @Named(MAIN)
    fun provideMainScheduler(): Scheduler {
        return RxJavaInterop.toV1Scheduler(AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    @Named(IO)
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }
}