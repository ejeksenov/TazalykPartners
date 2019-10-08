package kz.nextstep.tazalykpartners.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kz.nextstep.domain.usecase.partner.GetCurrentUserPartnerUseCase
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.login.LoginFragment
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getCurrentUserPartnerUseCase: GetCurrentUserPartnerUseCase

    lateinit var mainApplication: MainApplication
    lateinit var router: Router

    private val navigator = SupportAppNavigator(this, R.id.main_container)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainApplication = application as MainApplication

        mainApplication.appComponent.inject(this)

        router = MainApplication.INSTANCE?.getRouter()!!


        if (!getCurrentUserPartnerUseCase.execute()){
            router.navigateTo(SampleScreen(LoginFragment::class.java.name))
        } else {
            router.navigateTo(SampleScreen(PinListFragment::class.java.name))
        }


    }


    override fun onResume() {
        super.onResume()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        MainApplication.INSTANCE?.getRouter()?.exit()
    }
}
