package kz.nextstep.tazalykpartners.ui.pinAdmin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
import kz.nextstep.domain.model.UserPartner
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.adminProfile.AdminProfileFragment
import kz.nextstep.tazalykpartners.ui.editProfile.EditProfileActivity
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.ui.filterByType.FilterByTypeActivity
import kz.nextstep.tazalykpartners.ui.login.LoginActivity
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerViewModel
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsFragment
import kz.nextstep.tazalykpartners.ui.userInteractivity.UserInteractivityFragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class PinAdminActivity : BaseNavigationViewActivity() {

    companion object {
        var userPartner = UserPartner()
    }

    private lateinit var pinAdminViewModel: PinAdminViewModel

    lateinit var router: Router
    lateinit var toolbar: Toolbar
    lateinit var navView: BottomNavigationView

    private var navItemIndex = 0

    val userInterActivityF = UserInteractivityFragment.newInstance()
    val statisticsF = StatisticsFragment.newInstance()
    val profileF = AdminProfileFragment.newInstance()

    private val navigator = SupportAppNavigator(this, R.id.container_pin_admin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_admin)

        router = MainApplication.INSTANCE?.getRouter()!!

        pinAdminViewModel = ViewModelProviders.of(this).get(PinAdminViewModel::class.java)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        selectToolbarMenuAndTitle()

        navView = findViewById(R.id.nv_pin_admin_bottom)

        router.replaceScreen(SampleScreen(userInterActivityF))

        pinAdminViewModel.getCurrentUserPartner()

        pinAdminViewModel.userPartnerLiveData.observe(this, Observer {
            if (it != null)
                userPartner = it
        })

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_user_interactivity -> {
                if (navItemIndex != 0) {
                    router.replaceScreen(SampleScreen(userInterActivityF))
                    navItemIndex = 0
                }
            }
            R.id.navigation_statistics -> {
                if (navItemIndex != 1) {
                    if (!userPartner.pinIds.isNullOrBlank()) {
                        val pinIds = userPartner.pinIds!!
                        val arg = bundleOf(AppConstants.PIN_ID to pinIds)
                        statisticsF.arguments = arg
                    }
                    if (navItemIndex == 0)
                        router.navigateTo(SampleScreen(statisticsF))
                    else
                        router.replaceScreen(SampleScreen(statisticsF))
                    navItemIndex = 1
                }
            }
            R.id.navigation_profile -> {
                if (navItemIndex != 2) {
                    if (navItemIndex == 0)
                        router.navigateTo(SampleScreen(profileF))
                    else
                        router.replaceScreen(SampleScreen(profileF))
                    navItemIndex = 2
                }
            }
        }
        selectToolbarMenuAndTitle()
        return@OnNavigationItemSelectedListener true
    }


    private fun selectToolbarMenuAndTitle() {
        invalidateOptionsMenu()
        when (navItemIndex) {
            0 -> {
                supportActionBar?.setTitle(R.string.nav_user_interactivity)
            }
            1 -> {
                supportActionBar?.setTitle(R.string.nav_statistics)
            }
            2 -> {
                supportActionBar?.setTitle(R.string.nav_profile)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (navItemIndex) {
            0 -> menuInflater.inflate(R.menu.menu_user_interactivity, menu)
            1 -> menuInflater.inflate(R.menu.navigation_drawer, menu)
            2 -> menuInflater.inflate(R.menu.menu_sign_out, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_by_date -> {
                val intent = Intent(this, FilterByDateActivity::class.java)
                intent.putExtra(AppConstants.SELECTED_DATES, selectedDates)
                intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, selectedFilterType)
                startActivityForResult(intent, AppConstants.REQUEST_CODE)
                true
            }
            R.id.action_filter_by_waste -> {
                val intent = Intent(this, FilterByTypeActivity::class.java)
                    .putExtra(AppConstants.SELECTED_WASTE_ID, selectedWasteId)
                startActivityForResult(intent, 2)
                true
            }
            R.id.action_sign_out -> {
                onSignOutAlertDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSignOutAlertDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(resources.getText(R.string.ask_sign_out))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getText(R.string.yes)) { dialogInterface, i ->
            if (pinAdminViewModel.signOut()) {
                val intent = Intent(this, LoginActivity::class.java)
                router.newRootChain(SampleScreen(intent))
            }
            dialogInterface.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getText(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    fun goToEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        router.navigateTo(SampleScreen(intent))
    }

    override fun onBackPressed() {
        if (navItemIndex != 0) {
            navItemIndex = 0
            selectToolbarMenuAndTitle()
            navView.selectedItemId = R.id.navigation_user_interactivity
        }
        super.onBackPressed()
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
}
