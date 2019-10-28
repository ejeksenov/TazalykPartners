package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import kz.nextstep.tazalykpartners.R
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.content.res.ResourcesCompat
import android.graphics.Color
import androidx.core.os.bundleOf
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsFragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator


class PinDetailedInfoActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    var toolbarItem = 0
    var pinId = ""

    private val navigator = SupportAppNavigator(this, R.id.layout_pin_detailed_info_container)
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_detailed_info)

        router = MainApplication.INSTANCE?.getRouter()!!

        if (intent != null) {
            pinId = intent.getStringExtra(AppConstants.PIN_ID)!!
        }

        val fragment = PinDetailedInfoFragment.newInstance()
        val arg = bundleOf(AppConstants.PIN_ID to pinId)
        fragment.arguments = arg
        router.replaceScreen(SampleScreen(fragment))

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onManageBackPressed()
            super.onBackPressed()
        }

    }

    private fun onManageBackPressed() {
        if (toolbarItem == 1) {
            toolbarItem = 0
            setToolbarTitle()
            invalidateOptionsMenu()
        }
    }


    override fun onBackPressed() {
        onManageBackPressed()
        super.onBackPressed()
    }

    private fun setToolbarTitle() {
        if (toolbarItem == 0)
            toolbar.title = resources.getText(R.string.detailed_info)
        else
            toolbar.title = resources.getText(R.string.nav_statistics)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (toolbarItem == 0) {
            menuInflater.inflate(R.menu.activity_pin_detailed_info_menu, menu)
            onChangeIconTint()
        } else
            menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    private fun onChangeIconTint() {
        var drawable = ResourcesCompat.getDrawable(resources, R.drawable.statistics_logo, null)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable!!, Color.WHITE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_pin_data -> {
                val intent = Intent(this, AddEditPinActivity::class.java)
                intent.putExtra(AppConstants.PIN_ID, pinId)
                startActivity(intent)
                true
            }
            R.id.action_pin_statistics -> {
                toolbarItem = 1
                setToolbarTitle()
                invalidateOptionsMenu()
                val fragment = StatisticsFragment.newInstance()
                val arg = bundleOf(AppConstants.PIN_ID to pinId)
                fragment.arguments = arg
                router.navigateTo(SampleScreen(fragment))
                true
            }
            R.id.action_filter -> {
                val intent = Intent(this, FilterByDateActivity::class.java)
                intent.putExtra(AppConstants.SELECTED_DATES, StatisticsFragment.selectedDates)
                intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, StatisticsFragment.selectedFilterType)
                startActivityForResult(intent, AppConstants.REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

    }
}
