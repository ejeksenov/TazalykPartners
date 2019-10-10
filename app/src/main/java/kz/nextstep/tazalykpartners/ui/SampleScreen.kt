package kz.nextstep.tazalykpartners.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class SampleScreen(arg: Any): SupportAppScreen() {
    private var fragment: Fragment? = null
    private var intent: Intent? = null

    init {
        when(arg) {
            is Fragment -> fragment = arg
            is Context -> getActivityIntent(arg)
            is Intent -> intent = arg
        }
    }

    override fun getFragment() = fragment

    override fun getActivityIntent(context: Context?) = intent
}