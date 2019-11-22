package kz.nextstep.tazalykpartners.ui.addEditPin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.squareup.picasso.Picasso
import kz.nextstep.tazalykpartners.MainApplication

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pin
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pinImagesUriList
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pinLogoUri

class PinPhotosFragment : Fragment() {

    companion object {
        fun newInstance() = PinPhotosFragment()
    }

    private val REQUEST_CAMERA = 1

    private lateinit var layoutPinPhotosLogo: ViewGroup
    private lateinit var ivPinPhotosLogo: ImageView
    private lateinit var btnPinPhotosLogoDelete: ImageButton
    private lateinit var btnPinPhotosAddLogo: ImageButton

    private lateinit var rvPinPhotos: RecyclerView
    private lateinit var btnPinPhotosAddPhoto: ImageButton

    private lateinit var btnPinPhotosSave: Button

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    /*
    * imageIndex for logo  -> 0
    * imageIndex for photo1  -> 1
    * imageIndex for photo2  -> 2
    * imageIndex for photo3  -> 3
    * imageIndex for list of photos  -> 4
    */
    var imageIndex = -1

    private var logoUri: Uri? = null
    private var deletedLogoUrl = ""
    private val addedImageUriList: MutableList<Uri> = ArrayList()
    private val deletedImagesList: MutableList<String> = ArrayList()

    lateinit var pinLogo: Any
    private val pinImages = pin.imageLink
    private var pinImagesList: MutableList<Any> = ArrayList()


    private val adapter = PinPhotosAdapter(pinImagesList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_photos, container, false)

        layoutPinPhotosLogo = view.findViewById(R.id.layout_pin_photos_logo)

        ivPinPhotosLogo = view.findViewById(R.id.iv_pin_photos_logo)

        btnPinPhotosLogoDelete = view.findViewById(R.id.btn_pin_photos_logo_delete)

        btnPinPhotosAddLogo = view.findViewById(R.id.btn_pin_photos_add_logo)

        rvPinPhotos = view.findViewById(R.id.rv_pin_photos)
        btnPinPhotosAddPhoto = view.findViewById(R.id.btn_pin_photos_add_photo)

        btnPinPhotosSave = view.findViewById(R.id.btn_pin_photos_save)

        onSetAdapter()

        pinLogo = pin.logo!!
        if (pinLogo.toString().isNotBlank())
            Picasso.get().load(pinLogo.toString()).placeholder(R.drawable.loading_image).into(ivPinPhotosLogo)
        onCheckLogo()

        if (!pinImages.isNullOrBlank()) {
            for (item in pinImages.split(";")) {
                if (item.isNotBlank())
                    pinImagesList.add(item)
            }
            adapter.notifyDataSetChanged()
        }
        onCheckPhotosListSize()

        ivPinPhotosLogo.setOnClickListener {
            imageIndex = 11
            onOpenMediaOrCamera()
        }

        btnPinPhotosLogoDelete.setOnClickListener {
            if (pinLogo is String && pinLogo.toString().isNotBlank())
                deletedLogoUrl = pinLogo.toString()
            pinLogo = ""
            onCheckLogo()
        }

        btnPinPhotosAddLogo.setOnClickListener {
            imageIndex = 11
            onOpenMediaOrCamera()
        }

        btnPinPhotosAddPhoto.setOnClickListener {
            imageIndex = 4
            onOpenMediaOrCamera()
        }

        btnPinPhotosSave.setOnClickListener {
            if (logoUri != null)
                pinLogoUri = logoUri
            if (deletedLogoUrl.isNotBlank())
                AddEditPinActivity.deletedLogoUrl = deletedLogoUrl
            if (addedImageUriList.isNotEmpty())
                pinImagesUriList = addedImageUriList
            if (deletedImagesList.isNotEmpty())
                AddEditPinActivity.deletedImagesList = deletedImagesList
            activity!!.onBackPressed()
        }


        return view
    }

    private fun onOpenMediaOrCamera() {
        if (checkPermission()) {
            if (imageIndex == 4) {
                val size = if (pinImagesList.isNullOrEmpty()) 3 else 3 - pinImagesList.size
                val options = Options.init().setRequestCode(imageIndex).setCount(size)
                Pix.start(activity, options)
            } else
                Pix.start(activity, imageIndex)
        } else
            requestPermission()
    }

    private fun onSetAdapter() {
        rvPinPhotos.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        rvPinPhotos.layoutManager = LinearLayoutManager(context)
        rvPinPhotos.adapter = adapter
        adapter.onItemClick = { imageUrl, position, deleteOrAdd ->
            if (deleteOrAdd) {
                if (checkPermission()) {
                    imageIndex = position + 1
                    Pix.start(activity, imageIndex)
                } else
                    requestPermission()
            } else {
                pinImagesList.remove(imageUrl)
                if (imageUrl is String)
                    deletedImagesList.add(imageUrl)
                if (addedImageUriList.contains(imageUrl))
                    addedImageUriList.remove(imageUrl)
                adapter.notifyDataSetChanged()
                onCheckPhotosListSize()
            }
        }
    }


    private fun onCheckLogo() {
        if (pinLogo.toString().isNotBlank()) {
            layoutPinPhotosLogo.visibility = View.VISIBLE
            btnPinPhotosAddLogo.visibility = View.GONE
        } else {
            btnPinPhotosAddLogo.visibility = View.VISIBLE
            layoutPinPhotosLogo.visibility = View.GONE
        }
    }

    private fun onCheckPhotosListSize() {
        if (!pinImagesList.isNullOrEmpty() && pinImagesList.size == 3)
            btnPinPhotosAddPhoto.visibility = View.GONE
        else
            btnPinPhotosAddPhoto.visibility = View.VISIBLE
    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            MainApplication.INSTANCE?.applicationContext!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, imageIndex)
                } else {
                    Toast.makeText(
                        MainApplication.INSTANCE?.applicationContext!!,
                        resources.getString(R.string.permission_denied_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            if (!returnValue.isNullOrEmpty()) {
                when (requestCode) {
                    11 -> {
                        if (pinLogo is String && pinLogo.toString().isNotBlank())
                            deletedLogoUrl = pinLogo.toString()
                        pinLogo = returnValue[0]
                        val uri = Uri.parse(returnValue[0])
                        if (logoUri != uri) {
                            logoUri = uri
                            ivPinPhotosLogo.setImageURI(uri)
                        }
                    }
                    1,2,3 -> {
                        val imageUri = Uri.parse(returnValue[0])
                        if (pinImagesList[requestCode - 1] is String)
                            deletedImagesList.add(pinImagesList[requestCode - 1].toString())
                        if (!addedImageUriList.contains(imageUri)) {
                            addedImageUriList.add(imageUri)
                            pinImagesList[requestCode - 1] = imageUri
                            adapter.notifyDataSetChanged()
                        }
                    }
                    4 -> {
                        for (item in returnValue) {
                            val imageUri = Uri.parse(item)
                            if (!addedImageUriList.contains(imageUri)) {
                                addedImageUriList.add(imageUri)
                                pinImagesList.add(imageUri)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
                onCheckLogo()
                onCheckPhotosListSize()
                for (item in addedImageUriList) {
                    Log.e("ImageUri", item.toString())
                }
            }
        }
    }

}