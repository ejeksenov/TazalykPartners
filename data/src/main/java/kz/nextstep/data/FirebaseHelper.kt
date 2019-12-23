package kz.nextstep.data

import android.app.Application
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    fun setPersistenceEnabled() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    fun playServiceStatus(mainApplication: Application): Boolean {
        val playService = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mainApplication)
        return playService == ConnectionResult.SUCCESS
    }
}