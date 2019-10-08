package kz.nextstep.tazalykpartners.ui

import androidx.fragment.app.Fragment
import kz.nextstep.tazalykpartners.ui.login.LoginFragment
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class SampleScreen(private val fragmentName: String): SupportAppScreen() {

    private var fragment: Fragment? = null

    init {
        when(fragmentName) {
            LoginFragment::class.java.name -> fragment = LoginFragment.newInstance()
            PinListFragment::class.java.name -> fragment = PinListFragment.newInstance()
        }
    }

    override fun getFragment() = fragment
}