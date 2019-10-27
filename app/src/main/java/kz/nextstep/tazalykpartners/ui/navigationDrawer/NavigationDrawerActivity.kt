package kz.nextstep.tazalykpartners.ui.navigationDrawer

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.editProfile.EditProfileActivity
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.ui.filterByType.FilterByTypeActivity
import kz.nextstep.tazalykpartners.ui.login.LoginActivity
import kz.nextstep.tazalykpartners.ui.pinlist.PinListAdapter
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsFragment
import kz.nextstep.tazalykpartners.utils.CircleTransform


class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var router: Router
    lateinit var navigationDrawerViewModel: NavigationDrawerViewModel
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var toolbar: Toolbar
    lateinit var iv_main_nav_profile: ImageView
    lateinit var tv_main_nav_name: TextView
    lateinit var layout_main_drawer: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var navHeader: View

    private var navItemIndex = 0
    private var cntPressed = 0

    val REQUEST_CODE = 1

    private val navigator = SupportAppNavigator(this, R.id.activity_navigation_drawer_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        layout_main_drawer = findViewById(R.id.layout_main_drawer)
        navView = findViewById(R.id.nav_view)
        navHeader = navView.getHeaderView(0)
        iv_main_nav_profile = navHeader.findViewById(R.id.iv_main_nav_profile)
        tv_main_nav_name = navHeader.findViewById(R.id.tv_main_nav_name)
        toggle = ActionBarDrawerToggle(
            this, layout_main_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        layout_main_drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        router = MainApplication.INSTANCE?.getRouter()!!

        navigationDrawerViewModel = ViewModelProviders.of(this).get(NavigationDrawerViewModel::class.java)

        loadNavHeader()
        selectNavMenu()
        selectToolbarTitle()
        router.replaceScreen(SampleScreen(PinListFragment.newInstance()))

        pinItemStatitisticsClicked()

    }

    private fun pinItemStatitisticsClicked() {
        PinListAdapter.onStatisticsBtnClick = {
            cntPressed++
            navItemIndex = 3
            val fragment = StatisticsFragment.newInstance()
            val arg = bundleOf(AppConstants.PIN_ID to it)
            fragment.arguments = arg
            router.navigateTo(SampleScreen(fragment))
            navView.menu.getItem(0).isChecked = false
            selectToolbarTitle()
        }
    }

    private fun loadNavHeader() {
        navigationDrawerViewModel.getCurrentUserPartner()
        navigationDrawerViewModel.userPartnerLiveData.observe(this, Observer {
            val imageUrl = it.imageUrl
            if (imageUrl != null && imageUrl != "")
                Picasso.get().load(imageUrl).transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(
                    iv_main_nav_profile
                )
            tv_main_nav_name.text = it.name
        })
    }

    override fun onBackPressed() {
        if (layout_main_drawer.isDrawerOpen(GravityCompat.START)) {
            layout_main_drawer.closeDrawer(GravityCompat.START)
        } else {
            toggle.isDrawerIndicatorEnabled = true
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            toggle.syncState()
            if (cntPressed > 0) {
                cntPressed--
                navItemIndex = if (navItemIndex == 0) 1 else 0
                selectNavMenu()
                selectToolbarTitle()
            }
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_filter -> {
                var intent = if (navItemIndex == 0) {
                    Intent(this, FilterByTypeActivity::class.java)
                } else {
                    Intent(this, FilterByDateActivity::class.java)
                }
                startActivityForResult(intent, REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_pin_list -> {
                if (navItemIndex != 0) {
                    cntPressed++
                    navItemIndex = 0
                    if (cntPressed >= 2) {
                        cntPressed = 0
                        router.exit()
                    } else
                        router.navigateTo(SampleScreen(PinListFragment.newInstance()))
                }
            }
            R.id.nav_statistics -> {
                if (navItemIndex != 1) {
                    if (navItemIndex == 3)
                        router.replaceScreen(SampleScreen(StatisticsFragment.newInstance()))
                    else {
                        cntPressed++
                        router.navigateTo(SampleScreen(StatisticsFragment.newInstance()))
                    }
                    navItemIndex = 1
                }
            }
            R.id.nav_edit_profile -> {
                goToEditProfileAcitivity()
                layout_main_drawer.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_sign_out -> {
                signOutDialog()
                return false
            }
            else -> {
                navItemIndex = 0
            }
        }
        selectNavMenu()
        selectToolbarTitle()
        layout_main_drawer.closeDrawer(GravityCompat.START)

        return true
    }

    private fun selectNavMenu() {
        navView.menu.getItem(navItemIndex).isChecked = true
    }

    private fun selectToolbarTitle() {
        if (navItemIndex == 0)
            supportActionBar?.setTitle(R.string.nav_pin_list)
        else
            supportActionBar?.setTitle(R.string.nav_statistics)
    }

    private fun signOutDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(resources.getText(R.string.ask_sign_out))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getText(R.string.yes)) { dialogInterface, i ->
            if (navigationDrawerViewModel.signOut()) {
                val intent = Intent(this, LoginActivity::class.java)
                router.newRootChain(SampleScreen(intent))
            }
            dialogInterface.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getText(R.string.no)) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    private fun goToEditProfileAcitivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        router.navigateTo(SampleScreen(intent))
    }


    override fun onResume() {
        super.onResume()
        selectNavMenu()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }
}
