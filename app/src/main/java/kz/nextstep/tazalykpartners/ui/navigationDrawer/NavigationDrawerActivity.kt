package kz.nextstep.tazalykpartners.ui.navigationDrawer

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.AppConstants.REQUEST_CODE
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.editProfile.EditProfileActivity
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.ui.filterByType.FilterByTypeActivity
import kz.nextstep.tazalykpartners.ui.login.LoginActivity
import kz.nextstep.tazalykpartners.ui.pinlist.PinListAdapter
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsFragment
import kz.nextstep.tazalykpartners.ui.userInteractivity.UserInteractivityFragment
import kz.nextstep.tazalykpartners.utils.CircleTransform


class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var selectedWasteId = ""
        var selectedDates = ""
        var selectedFilterType = "За месяц"
        var filterDateDays = 30
    }

    lateinit var router: Router
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navigationDrawerViewModel: NavigationDrawerViewModel

    lateinit var toolbar: Toolbar
    lateinit var ivMainNavProfile: ImageView
    lateinit var tvMainNavName: TextView
    lateinit var layoutMainDrawer: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var navHeader: View

    private var navItemIndex = 0
    private var cntPressed = 0


    private val navigator = SupportAppNavigator(this, R.id.activity_navigation_drawer_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        layoutMainDrawer = findViewById(R.id.layout_main_drawer)
        navView = findViewById(R.id.nav_view)
        navHeader = navView.getHeaderView(0)
        ivMainNavProfile = navHeader.findViewById(R.id.iv_main_nav_profile)
        tvMainNavName = navHeader.findViewById(R.id.tv_main_nav_name)
        toggle = ActionBarDrawerToggle(
            this, layoutMainDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        layoutMainDrawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        router = MainApplication.INSTANCE?.getRouter()!!

        navigationDrawerViewModel = ViewModelProviders.of(this).get(NavigationDrawerViewModel::class.java)

        loadNavHeader()
        selectNavMenu()
        selectToolbarTitle()
        router.replaceScreen(SampleScreen(UserInteractivityFragment.newInstance()))

        pinItemStatisticsClicked()

    }

    private fun pinItemStatisticsClicked() {
        PinListAdapter.onStatisticsBtnClick = {
            cntPressed++
            navItemIndex = 3
            val fragment = StatisticsFragment.newInstance()
            val arg = bundleOf(AppConstants.PIN_ID to it)
            fragment.arguments = arg
            router.navigateTo(SampleScreen(fragment))
            navView.menu.getItem(1).isChecked = false
            selectToolbarTitle()
        }
    }

    private fun loadNavHeader() {
        navigationDrawerViewModel.getCurrentUserPartner()
        navigationDrawerViewModel.userPartnerLiveData.observe(this, Observer {
            val imageUrl = it.imageUrl
            if (imageUrl != null && imageUrl != "")
                Picasso.get().load(imageUrl).transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(
                    ivMainNavProfile
                )
            tvMainNavName.text = it.name
        })
    }

    override fun onBackPressed() {
        if (layoutMainDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutMainDrawer.closeDrawer(GravityCompat.START)
        } else {

            if (cntPressed > 0) {
                cntPressed--
                navItemIndex = if (navItemIndex == 3) 1 else 0
                selectNavMenu()
                selectToolbarTitle()
            }
            super.onBackPressed()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        when (navItemIndex) {
            0 -> menuInflater.inflate(R.menu.menu_user_interactivity, menu)
            1 -> menu.clear()
            else -> menuInflater.inflate(R.menu.navigation_drawer, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_filter_by_date -> {
                val intent = Intent(this, FilterByDateActivity::class.java)
                intent.putExtra(AppConstants.SELECTED_DATES, selectedDates)
                intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, selectedFilterType)
                startActivityForResult(intent, REQUEST_CODE)
                true
            }
            R.id.action_filter_by_waste -> {
                val intent = Intent(this, FilterByTypeActivity::class.java)
                    .putExtra(AppConstants.SELECTED_WASTE_ID, selectedWasteId)
                startActivityForResult(intent, 2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (navItemIndex == 3)
            router.exit()
        when (item.itemId) {
            R.id.nav_user_interactivity -> {
                if (navItemIndex != 0) {
                    cntPressed++
                    navItemIndex = 0
                    val fragment = UserInteractivityFragment.newInstance()
                    onPutPinIdExtra(fragment)
                    router.replaceScreen(SampleScreen(fragment))

                }
            }
            R.id.nav_pin_list -> {
                if (navItemIndex != 1) {
                    cntPressed++
                    //when back pressed go to initial UserInteractivity screen
                    if (navItemIndex == 0)
                        router.navigateTo(SampleScreen(PinListFragment.newInstance()))
                    else
                        router.replaceScreen(SampleScreen(PinListFragment.newInstance()))
                    navItemIndex = 1

                }
            }
            R.id.nav_statistics -> {
                if (navItemIndex != 2) {
                    val fragment = StatisticsFragment.newInstance()
                    onPutPinIdExtra(fragment)
                    cntPressed++
                    if (navItemIndex == 0)
                        router.navigateTo(SampleScreen(fragment))
                    else
                        router.replaceScreen(SampleScreen(fragment))
                    navItemIndex = 2
                }
            }
            R.id.nav_edit_profile -> {
                goToEditProfileActivity()
                layoutMainDrawer.closeDrawer(GravityCompat.START)
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
        layoutMainDrawer.closeDrawer(GravityCompat.START)

        return true
    }

    private fun onPutPinIdExtra(fragment: Fragment) {
        navigationDrawerViewModel.userPartnerPinIdsLiveData.observe(this, Observer {
            val arg = bundleOf(AppConstants.PIN_ID to it)
            fragment.arguments = arg
        })
    }

    private fun selectNavMenu() {
        navView.menu.getItem(navItemIndex).isChecked = true
    }

    private fun selectToolbarTitle() {
        invalidateOptionsMenu()
        when (navItemIndex) {
            0 -> supportActionBar?.setTitle(R.string.nav_user_interactivity)
            1 -> supportActionBar?.setTitle(R.string.nav_pin_list)
            2, 3 -> supportActionBar?.setTitle(R.string.nav_statistics)
        }
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

    private fun goToEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        router.navigateTo(SampleScreen(intent))
    }


    override fun onResume() {
        super.onResume()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    /*toggle.isDrawerIndicatorEnabled = true
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            toggle.syncState()*/
}
