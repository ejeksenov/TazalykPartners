package kz.nextstep.tazalykpartners.ui.addEditPin

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kz.nextstep.domain.model.Pin
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.filterByType.FilterByTypeActivity
import kz.nextstep.tazalykpartners.utils.CustomProgressBar
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AddEditPinActivity : AppCompatActivity() {

    companion object {
        var pin = Pin()
        var pinLogoUri: Uri? = null
        var pinImagesUriList: MutableList<Uri> = ArrayList()
        var deletedLogoUrl = ""
        var deletedImagesList: MutableList<String> = ArrayList()
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

        if (intent != null && intent.getStringExtra(AppConstants.PIN_ID) != null)
            pinId = intent.getStringExtra(AppConstants.PIN_ID)!!

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
            pin = Pin()
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
                onClearData()
                finish()

            }
        })

        toolbar.setNavigationOnClickListener {
            onManageBackPressed()
        }
    }

    private fun onClearData() {
        pin = Pin()
        pinLogoUri = null
        pinImagesUriList.clear()
        deletedLogoUrl = ""
        deletedImagesList.clear()
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

        addEditPinViewModel.deletePinImages(deletedLogoUrl, deletedImagesList)

        addEditPinViewModel.deletePinImagesMutableLiveData.observe(this, Observer {
            if (it) {
                if (deletedLogoUrl.isNotBlank())
                    pin.logo = ""
                if (deletedImagesList.isNotEmpty()) {
                    var imageUrls = pin.imageLink!!
                    for (item in deletedImagesList) {
                        if (imageUrls.contains(item))
                            imageUrls = imageUrls.replace("$item;", "")
                    }
                    pin.imageLink = imageUrls
                }
                addEditPinViewModel.uploadPinImages(pinLogoUri, pinImagesUriList, pinId)
            }

        })

        addEditPinViewModel.uploadPinImagesMutableLiveData.observe(this, Observer {
            if (!it.isNullOrBlank()) {
                val imageUrl = it
                if (imageUrl.contains(AppConstants.PICTURE_TYPE_LOGO))
                    pin.logo = imageUrl
                else if (!pin.imageLink!!.contains(imageUrl))
                    pin.imageLink += "$imageUrl;"
            }
        })

        addEditPinViewModel.uploadPinImagesDoneMutableLiveData.observe(this, Observer {
            if (it)
                onAddOrUpdatePinData()
        })


    }

    private fun onAddOrUpdatePinData() {
        if (pinId.isBlank())
            addEditPinViewModel.addNewPin(pin)
        else
            addEditPinViewModel.updatePinData(pinId, pin)
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
        val mutableWasteIdList: MutableList<String> = resources.getStringArray(R.array.waste_id).toMutableList()
        val wasteId = pin.wasteId
        val intent = Intent(this, FilterByTypeActivity::class.java)
        if (!wasteId.isNullOrBlank()) {
            var convertedWasteId = ""
            for (item in mutableWasteIdList) {
                val itemArr = item.split(",")
                for (item2 in wasteId.split(";")) {
                    if (item2.contains("${itemArr[0]},${itemArr[1]}")) {
                        val item2Arr = item2.split(",")
                        convertedWasteId += "${item2Arr[0]},${item2Arr[1]},${item2Arr[2]},${itemArr[3]},${itemArr[4]};"
                        break
                    }
                }
            }
            intent.putExtra(AppConstants.SELECTED_WASTE_ID, convertedWasteId)
        }
        startActivityForResult(intent, AppConstants.REQUEST_CODE)
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

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}
