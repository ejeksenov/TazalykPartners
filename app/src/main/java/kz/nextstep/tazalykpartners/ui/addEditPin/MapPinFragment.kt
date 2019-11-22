package kz.nextstep.tazalykpartners.ui.addEditPin


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pin
import java.util.*

class MapPinFragment : Fragment(), OnMapReadyCallback, MapboxMap.OnMapClickListener {

    companion object {
        fun newInstance() = MapPinFragment()
    }

    private lateinit var edtMapPinAddress: EditText
    private lateinit var mvMapPin: MapView
    private lateinit var spMapPinCity: Spinner
    private lateinit var spMapPinCountry: Spinner
    private lateinit var tvMapPinLatLng: TextView
    private lateinit var btnMapPinSave: ImageButton
    private lateinit var btnMapPinZoomIn: ImageButton
    private lateinit var btnMapPinZoomOut: ImageButton
    private lateinit var btnMapPinCurrentLocation: ImageButton

    private lateinit var mapboxMap: MapboxMap

    private var zoomLevel = 10.0
    private var latLng = ""
    private var country = ""
    private var city = ""

    private lateinit var countries: Array<String>
    private lateinit var cities: Array<String>
    private var location: LatLng? = null

    private val locale = Locale("RU")
    lateinit var geoCoder: Geocoder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mContext = context
        if (mContext != null) {
            Mapbox.getInstance(mContext, resources.getString(R.string.mapbox_access_token))
            geoCoder = Geocoder(mContext, locale)
        }
        val view = inflater.inflate(R.layout.fragment_map_pin, container, false)

        tvMapPinLatLng = view.findViewById(R.id.tv_map_pin_lat_lng)
        edtMapPinAddress = view.findViewById(R.id.edt_map_pin_address)
        spMapPinCountry = view.findViewById(R.id.sp_map_pin_country)
        spMapPinCity = view.findViewById(R.id.sp_map_pin_city)
        btnMapPinSave = view.findViewById(R.id.btn_map_pin_save)
        btnMapPinCurrentLocation = view.findViewById(R.id.btn_map_pin_current_location)
        btnMapPinZoomIn = view.findViewById(R.id.btn_map_pin_zoom_in)
        btnMapPinZoomOut = view.findViewById(R.id.btn_map_pin_zoom_out)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mvMapPin = view.findViewById(R.id.mv_map_pin)
        mvMapPin.onCreate(savedInstanceState)
        mvMapPin.onResume()
        mvMapPin.getMapAsync(this)

        btnMapPinZoomIn.setOnClickListener {
            mapboxMap.animateCamera(CameraUpdateFactory.zoomIn())
            zoomLevel = mapboxMap.cameraPosition.zoom
        }

        btnMapPinZoomOut.setOnClickListener {
            mapboxMap.animateCamera(CameraUpdateFactory.zoomOut())
            zoomLevel = mapboxMap.cameraPosition.zoom
        }

        btnMapPinCurrentLocation.setOnClickListener {


            val style = mapboxMap.style
            if (style != null && onCheckGPS())
                enableLocationComponent(style)
        }

        btnMapPinSave.setOnClickListener {
            if (onCheckData())
                onSaveData()
        }

    }

    private fun onCheckData(): Boolean {
        edtMapPinAddress.error = null
        val address = edtMapPinAddress.text.toString()
        if (address.isBlank()) {
            edtMapPinAddress.requestFocus()
            edtMapPinAddress.error = resources.getString(R.string.enter_address)
            return false
        }
        if (latLng.isBlank()) {
            Toast.makeText(context, resources.getString(R.string.choose_location), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun onSaveData() {
        val address = edtMapPinAddress.text.toString()
        pin.country = country
        pin.city = city
        pin.address = address
        pin.latlng = latLng
        activity!!.onBackPressed()
    }

    private fun onSetCountriesAdapter() {
        countries = resources.getStringArray(R.array.countries)
        var countryArr = Array(countries.size) {
            countries[it].split(",")[0]
        }
        val countryAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, countryArr)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMapPinCountry.adapter = countryAdapter
        var selectionIndex = 0
        var countryName = ""
        if (!pin.country.isNullOrBlank()) {
            countryName = pin.country!!
        } else if (location != null) {
            countryName = onGetCountryName()
        }
        for ((index,item) in countryArr.withIndex()) {
            if (item.contains(countryName))
                selectionIndex = index
        }
        spMapPinCountry.setSelection(selectionIndex)
        spMapPinCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val cityArrayName = countries[position].split(",")[1]
                country = countryArr[position]
                onSetCityAdapter(cityArrayName)
            }

        }

    }

    private fun onGetCountryName(): String {
        try {
            val addressList = geoCoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
            if (!addressList.isNullOrEmpty())
                return addressList[0].countryName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun onSetCityAdapter(cityArrayName: String) {
        val cityArrayId = resources.getIdentifier(cityArrayName, "array", context!!.packageName)
        cities = resources.getStringArray(cityArrayId)
        val cityAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMapPinCity.adapter = cityAdapter
        var selectionIndex = 0
        var cityName = ""
        if (!pin.city.isNullOrBlank()) {
            cityName = pin.city!!
        } else if (location != null)
            cityName = onGetCityName()
        selectionIndex = cities.indexOf(cityName)
        spMapPinCity.setSelection(selectionIndex)

        spMapPinCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city = cities[position]
                onAnimateCameraToCity(city)
            }

        }

    }

    private fun onGetCityName(): String {
        try {
            val addressList = geoCoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
            if (!addressList.isNullOrEmpty())
                return addressList[0].locality
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }



    private fun onAnimateCameraToCity(city: String) {
        val locale = Locale("RU")
        val geoCoder = Geocoder(context, locale)
        val addresses = geoCoder.getFromLocationName(city, 1)
        if (addresses.isNotEmpty()) {
            for (item in addresses) {
                val latLong = LatLng(item.latitude, item.longitude)
                val cameraPositon = CameraPosition.Builder().target(latLong).zoom(10.0).build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositon))
            }
        }
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.Builder().fromUri(
                "mapbox://styles/mapbox/streets-v10"
            )
        ) {
            if (onCheckGPS())
                enableLocationComponent(mapboxMap.style!!)
        }

        onSetCountriesAdapter()

        onSetData()

        mapboxMap.addOnMapClickListener(this)
    }

    private fun onSetData() {
        if (!pin.latlng.isNullOrBlank()) {
            latLng = pin.latlng!!
            tvMapPinLatLng.text = latLng
            val latLngArr = pin.latlng!!.split(",")
            if (latLngArr.size >= 2) {
                val latitude = latLngArr[0].toDouble()
                val longitude = latLngArr[1].toDouble()
                val location = LatLng(latitude, longitude)
                onAddMarker(location)
            }
        }
        if (!pin.address.isNullOrBlank()) {
            edtMapPinAddress.setText(pin.address)
        }

    }


    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(it: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            val options = LocationComponentOptions.builder(context!!)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(context!!, R.color.mainBackgroundColor))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(context!!, it)
                .locationComponentOptions(options)
                .build()

            // Activate with options
            mapboxMap.locationComponent.apply {

                activateLocationComponent(locationComponentActivationOptions)

                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS

            }
            val lastKnownLocation = mapboxMap.locationComponent.lastKnownLocation
            if (lastKnownLocation != null) {
                location = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                val cameraPosition = CameraPosition.Builder().target(location).zoom(zoomLevel).build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun getAddressByLatLng(location: LatLng) {
        val locale = Locale("RU")
        val geoCoder = Geocoder(context, locale)
        try {
            val addressList = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList.isNotEmpty()) {
                for (item in addressList) {
                    val addressLine = item.getAddressLine(0)
                    if (!addressLine.isNullOrBlank()) {
                        val addressArr = addressLine.split(",")
                        if (addressArr.isNotEmpty()) {
                            val address = addressArr[0]
                            edtMapPinAddress.setText(address)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            edtMapPinAddress.text.clear()
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (onCheckGPS())
                        enableLocationComponent(mapboxMap.style!!)
                } else {
                    val activity = activity
                    if (activity != null) {
                        val title = resources.getString(R.string.permission_title)
                        val message = resources.getString(R.string.location_permission_message)
                        permissionSettings(activity, message, title)
                    }
                    //Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private fun permissionSettings(activity: Activity, message: String, title: String) {
        val showSettingsBtn = activity.resources.getString(R.string.show_settings)
        androidx.appcompat.app.AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(showSettingsBtn) { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivity(intent)
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    override fun onMapClick(point: LatLng): Boolean {
        latLng = "${point.latitude},${point.longitude}"
        tvMapPinLatLng.text = latLng
        getAddressByLatLng(point)
        onAddMarker(point)
        return true
    }

    private fun onAddMarker(point: LatLng) {
        val markers = mapboxMap.markers
        if (!markers.isNullOrEmpty()) {
            for (marker in markers) {
                mapboxMap.removeMarker(marker)
            }
        }
        mapboxMap.addMarker(MarkerOptions().position(point))
    }


    private fun onCheckGPS(): Boolean {
        val manager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSenabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val mActivity = activity
        if (!isGPSenabled && mActivity != null) {
            onAlertForEnablingGPS(mActivity)
        }
        return isGPSenabled
    }

    private fun onAlertForEnablingGPS(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        val message = resources.getString(R.string.enable_gps)

        builder.setMessage(message)
            .setPositiveButton(
                resources.getString(R.string.enable)
            ) { d, id ->
                activity.startActivity(Intent(action))
                d.dismiss()
                activity.recreate()
            }
            .setNegativeButton(
                resources.getString(R.string.cancel)
            ) { d, id -> d.cancel() }
        builder.create().show()
    }


    override fun onStart() {
        super.onStart()
        mvMapPin.onStart()
    }

    override fun onResume() {
        super.onResume()
        mvMapPin.onResume()
    }

    override fun onPause() {
        super.onPause()
        mvMapPin.onPause()
    }

    override fun onStop() {
        super.onStop()
        mvMapPin.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvMapPin.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvMapPin.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvMapPin.onLowMemory()
    }

}
