package kz.nextstep.tazalykpartners.ui.addEditPin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_add_edit_pin.*
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.filterByType.FilterByTypeActivity
import kz.nextstep.tazalykpartners.ui.userInteractivity.UserInteractivityFragment
import kz.nextstep.tazalykpartners.utils.CustomProgressBar
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AddEditPinActivity : AppCompatActivity() {

    companion object {
        var pin = Pin()
    }

    var pinId = ""

    private lateinit var router: Router
    lateinit var toolbar: Toolbar
    lateinit var addEditPinViewModel: AddEditPinViewModel
    lateinit var customProgressBar: CustomProgressBar

    private var navItemIndex = 0

    private val navigator = SupportAppNavigator(this, R.id.layout_add_edit_pin_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_pin)

        if (intent != null && intent.getStringExtra(AppConstants.PIN_ID) != null) {
            pinId = intent.getStringExtra(AppConstants.PIN_ID)!!
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        router = MainApplication.INSTANCE?.getRouter()!!
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        addEditPinViewModel = ViewModelProviders.of(this).get(AddEditPinViewModel::class.java)
        customProgressBar = CustomProgressBar(this)

        if (pinId.isNotBlank()) {
            addEditPinViewModel.getPinData(pinId)

            addEditPinViewModel.pinMutableLiveData.observe(this, Observer {
                pin = it
                selectToolbarTitle()
                router.replaceScreen(SampleScreen(MainPinInfoFragment.newInstance()))
            })
        } else {
            selectToolbarTitle()
            router.replaceScreen(SampleScreen(MainPinInfoFragment.newInstance()))
        }


        addEditPinViewModel.newPinMutableLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (it) {
                if (pinId.isNotBlank())
                    Toast.makeText(this, resources.getString(R.string.success_change_data), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, resources.getString(R.string.success_new_pin_added), Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        toolbar.setNavigationOnClickListener {
            onManageBackPressed()
        }
    }

    private fun onManageBackPressed() {
        if (navItemIndex != 0) {
            navItemIndex = 0
            selectToolbarTitle()
            invalidateOptionsMenu()
        }
        super.onBackPressed()
    }

    private fun selectToolbarTitle() {
        var title = ""
        when (navItemIndex) {
            0 -> {
                title =
                    if (pinId.isNotBlank()) resources.getString(R.string.edit) else resources.getString(R.string.add)
            }
            1 -> title = resources.getString(R.string.logo_and_photo)
            2 -> title = resources.getString(R.string.location)
            3 -> title = resources.getString(R.string.work_time)
        }
        supportActionBar?.title = title
    }

    fun onSaveData() {
        customProgressBar.show()
        if (pinId.isBlank() && onCheckFields())
            addEditPinViewModel.addNewPin(pin)
        else
            addEditPinViewModel.updatePinData(pinId, pin)
    }

    private fun onCheckFields(): Boolean {
        if (pin.address.isNullOrBlank()
            || pin.latlng.isNullOrBlank()
            || pin.country.isNullOrBlank()
            || pin.city.isNullOrBlank()) {
            showToastMessage(resources.getString(R.string.choose_location))
            return false
        }
        if (pin.workTime.isNullOrBlank()) {
            showToastMessage(resources.getString(R.string.choose_work_schedule))
            return false
        }
        if (pin.wasteId.isNullOrBlank()) {
            showToastMessage(resources.getString(R.string.choose_waste_type))
            return false
        }
        return true
    }


    fun goToMapPinFragment() {
        navItemIndex = 2
        val fragment = MapPinFragment.newInstance()
        router.navigateTo(SampleScreen(fragment))
        selectToolbarTitle()
        invalidateOptionsMenu()

    }

    fun goToPinPhotosFragment() {
        navItemIndex = 1
        val fragment = PinPhotosFragment.newInstance()
        router.navigateTo(SampleScreen(fragment))
        selectToolbarTitle()
        invalidateOptionsMenu()
    }

    fun goToPinWorkTimeFragment() {
        navItemIndex = 3
        val fragment = PinWorkTimeFragment.newInstance()
        router.navigateTo(SampleScreen(fragment))
        selectToolbarTitle()
        invalidateOptionsMenu()
    }

    fun goToFilterByType() {
        val intent = Intent(this, FilterByTypeActivity::class.java)
        startActivity(intent)
    }


    override fun onBackPressed() {
        onManageBackPressed()
    }


    override fun onResume() {
        super.onResume()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }

    private fun showToastMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()



    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}
