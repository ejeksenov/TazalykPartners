package kz.nextstep.tazalykpartners.ui.adminProfile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kz.nextstep.tazalykpartners.R
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.ui.addPointsToUser.AddPointsToUserActivity
import kz.nextstep.tazalykpartners.ui.pinAdmin.PinAdminActivity
import kz.nextstep.tazalykpartners.ui.pinAdmin.PinAdminActivity.Companion.userPartner
import kz.nextstep.tazalykpartners.utils.CircleTransform
import kz.nextstep.tazalykpartners.utils.CustomProgressBar


class AdminProfileFragment : Fragment() {

    companion object {
        fun newInstance() = AdminProfileFragment()
        var wasteType = ""
        var historyPinSize = 0
    }

    private lateinit var ivAdminProfileImage: ImageView
    private lateinit var tvAdminProfileName: TextView
    private lateinit var tvAdminProfileEmail: TextView
    private lateinit var btnAdminProfileEdit: Button
    private lateinit var btnAdminProfileAddPoints: Button
    private lateinit var edtAdminProfileEmailOrPhone: EditText
    private lateinit var ivAdminProfileVerificationQrCode: ImageView

    private lateinit var viewModel: AdminProfileViewModel
    private lateinit var customProgressBar: CustomProgressBar

    var pinId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_profile_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(AdminProfileViewModel::class.java)
        customProgressBar = CustomProgressBar(context!!)

        ivAdminProfileImage = view.findViewById(R.id.iv_admin_profile_image)
        tvAdminProfileName = view.findViewById(R.id.tv_admin_profile_name)
        tvAdminProfileEmail = view.findViewById(R.id.tv_admin_profile_email)
        btnAdminProfileEdit = view.findViewById(R.id.btn_admin_profile_edit)
        ivAdminProfileVerificationQrCode =
            view.findViewById(R.id.iv_admin_profile_verification_qr_code)
        btnAdminProfileAddPoints = view.findViewById(R.id.btn_admin_profile_add_points)
        edtAdminProfileEmailOrPhone = view.findViewById(R.id.edt_admin_profile_email_or_phone)

        onAssumeUserPartnerData()


        btnAdminProfileEdit.setOnClickListener {
            val activity = activity as PinAdminActivity
            activity.goToEditProfileActivity()
        }

        btnAdminProfileAddPoints.setOnClickListener {
            onAddPointsToUser()
        }

        return view
    }

    private fun onAddPointsToUser() {
        edtAdminProfileEmailOrPhone.error = null
        val emailOrPhone = edtAdminProfileEmailOrPhone.text.toString()

        if (emailOrPhone.isBlank()) {
            edtAdminProfileEmailOrPhone.error =
                context!!.resources.getString(R.string.enter_email_or_phone)
            return
        }
        customProgressBar.show()
        when {
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches() -> viewModel.getUserByEmail(
                emailOrPhone
            )
            android.util.Patterns.PHONE.matcher(emailOrPhone).matches() -> {
                val phone = when (val firstChar = emailOrPhone[0]) {
                    '8' -> emailOrPhone.replaceFirst(firstChar.toString(), "+7")
                    '7' -> emailOrPhone.replaceFirst(firstChar.toString(), "+7")
                    else -> emailOrPhone
                }
                viewModel.getUserByPhone(phone)
            }
            else -> {
                customProgressBar.dismiss()
                edtAdminProfileEmailOrPhone.error =
                    context!!.resources.getString(R.string.enter_correct_email_or_phone)
                return
            }
        }

        viewModel.userMutableLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (!it.isNullOrBlank()) {
                val intent = Intent(activity, AddPointsToUserActivity::class.java)
                intent.putExtra(AppConstants.PIN_ID, pinId)
                intent.putExtra(AppConstants.USER_ID, it)
                intent.putExtra(AppConstants.SELECTED_WASTE_ID, wasteType)
                startActivity(intent)
            }
        })


    }

    private fun onAssumeUserPartnerData() {
        val imageUrl = userPartner.imageUrl
        val adminName = userPartner.name
        val adminEmail = userPartner.email
        pinId = userPartner.pinIds

        if (!pinId.isNullOrBlank())
            viewModel.getHistoryPinSize(pinId!!)

        if (!imageUrl.isNullOrBlank())
            Picasso.get().load(imageUrl).transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(
                ivAdminProfileImage
            )
        if (!adminName.isNullOrBlank())
            tvAdminProfileName.text = adminName
        if (!adminEmail.isNullOrBlank())
            tvAdminProfileEmail.text = adminEmail
        if (!pinId.isNullOrBlank())
            viewModel.getPinById(pinId!!)


        viewModel.pinMutableLiveData.observe(this, Observer {
            val verificationQrCode = it.verificationCode
            wasteType = it.wasteId!!
            if (!verificationQrCode.isNullOrBlank())
                onViewQRCode(verificationQrCode)
        })

        viewModel.historyPinSize.observe(this, Observer {
            if (historyPinSize != it && !pinId.isNullOrBlank()) {
                viewModel.generatePinVerificationCode(pinId!!)
                historyPinSize = it
            }
        })
    }


    private fun onViewQRCode(verificationQrCode: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap =
                barcodeEncoder.encodeBitmap(verificationQrCode, BarcodeFormat.QR_CODE, 800, 800)
            ivAdminProfileVerificationQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.userMutableLiveData.removeObservers(this)
    }

}
