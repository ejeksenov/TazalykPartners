package kz.nextstep.tazalykpartners.ui.editProfile

import android.Manifest
import android.Manifest.permission.CAMERA
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.change_user_data_fragment.*
import kz.nextstep.tazalykpartners.MainApplication

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CircleTransform
import kz.nextstep.tazalykpartners.utils.CustomProgressBar
import java.io.File

class ChangeUserDataFragment : Fragment() {

    private val REQUEST_CAMERA = 1
    private val CAMERA_REQUEST_CODE = 100

    companion object {
        fun newInstance() = ChangeUserDataFragment()
    }

    private lateinit var viewModel: ChangeUserDataViewModel

    private lateinit var tvChangeUserDataChangeProfile: TextView
    private lateinit var tvChangeUserDataEmail: TextView
    private lateinit var tvChangeUserDataPassword: TextView
    private lateinit var edtChangeUserDataName: EditText
    private lateinit var ivChangeUserDataProfile: ImageView
    private lateinit var btnChangeUserDataSave: Button

    private lateinit var customProgressBar: CustomProgressBar

    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        CAMERA
    )

    var fullName = ""
    var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.change_user_data_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(ChangeUserDataViewModel::class.java)

        customProgressBar = CustomProgressBar(context!!)

        tvChangeUserDataChangeProfile = view.findViewById(R.id.tv_change_user_data_change_profile)
        ivChangeUserDataProfile = view.findViewById(R.id.iv_change_user_data_profile)
        tvChangeUserDataEmail = view.findViewById(R.id.tv_change_user_data_email)
        tvChangeUserDataPassword = view.findViewById(R.id.tv_change_user_data_password)
        edtChangeUserDataName = view.findViewById(R.id.edt_change_user_data_name)
        btnChangeUserDataSave = view.findViewById(R.id.btn_change_user_data_save)

        viewModel.getUserPartnerData()

        viewModel.userPartnerLiveData.observe(this, Observer {
            if (!it.imageUrl.isNullOrBlank())
                Picasso.get().load(it.imageUrl).fit().transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(
                    ivChangeUserDataProfile
                )
            edtChangeUserDataName.setText(it.name)
        })

        edtChangeUserDataName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fullName = p0.toString()
            }

        })


        btnChangeUserDataSave.setOnClickListener {
            if (!fullName.isBlank() || !imageUrl.isBlank()) {
                customProgressBar.show()
                viewModel.saveUserPartnerData(imageUrl, fullName)
            }
        }

        tvChangeUserDataEmail.setOnClickListener {
            if (activity != null)
                (activity as EditProfileActivity).goToChangeEmail()
        }

        tvChangeUserDataPassword.setOnClickListener {
            if (activity != null)
                (activity as EditProfileActivity).goToChangePassword()
        }

        tvChangeUserDataChangeProfile.setOnClickListener {
            if (checkPermission())
                Pix.start(this, CAMERA_REQUEST_CODE)
            else
                requestPermission()
        }

        viewModel.changeUserPartnerLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (it) {
                Toast.makeText(
                    MainApplication.INSTANCE?.applicationContext,
                    resources.getString(R.string.success_change_data),
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            } else
                Toast.makeText(
                    MainApplication.INSTANCE?.applicationContext,
                    resources.getString(R.string.error_change_data),
                    Toast.LENGTH_SHORT
                ).show()

        })

        return view
    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            MainApplication.INSTANCE?.applicationContext!!,
            CAMERA
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
                    Pix.start(this, CAMERA_REQUEST_CODE)
                } else {
                    Toast.makeText(
                        MainApplication.INSTANCE?.applicationContext!!,
                        resources.getString(R.string.permission_denied_message),
                        Toast.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel(resources.getString(R.string.give_both_permissions),
                                DialogInterface.OnClickListener { _, _ ->
                                    requestPermissions(
                                        permissions,
                                        REQUEST_CAMERA
                                    )
                                }
                            )
                            return
                        }
                    }
                }
                return
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        androidx.appcompat.app.AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton("Ок", okListener)
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            if (returnValue!![0] != null) {
                val imageUri = Uri.fromFile(File(returnValue[0]))
                ivChangeUserDataProfile.setImageURI(imageUri)
                imageUrl = imageUri.toString()
            }
        }
    }

}
