package kz.nextstep.tazalykpartners.utils

import android.content.Context

object SharedPrefManager {

    const val SHARED_PREFS_FILE_NAME = "kz.nextstep.tazalykpartners.sharedprefs"
    const val PREF_EMAIL_VERIFICATION = "email_verification"


    const val NOT_VERIFIED_VALUE = "not_verified"


    fun readSharedSetting(ctx: Context, settingName: String, defaultValue: String): String? {
        val sharedPref = ctx.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(settingName, defaultValue)
    }

    fun saveSharedSetting(ctx: Context, settingName: String, settingValue: String) {
        val sharedPref = ctx.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(settingName, settingValue)
        editor.apply()
    }
}