package kz.nextstep.tazalykpartners.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import kz.nextstep.tazalykpartners.utils.CircleTransform
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    lateinit var router: Router

    lateinit var mainViewModel: MainViewModel

    private val navigator = SupportAppNavigator(this, R.id.main_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        router = MainApplication.INSTANCE?.getRouter()!!

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)



    }

    private fun loadFragments() {
        router.navigateTo(SampleScreen(PinListFragment.newInstance()))



        mainViewModel.getCurrentUserPartner()

        mainViewModel.userPartnerLiveData.observe(this, Observer {
            //if (!it.imageUrl.equals(""))
              //  Picasso.get().load(it.imageUrl).placeholder(R.drawable.user_placeholder_image).transform(CircleTransform()).into(iv_main_nav_profile)
            //tv_main_nav_name.text = it.name
        })

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
