package kz.nextstep.tazalykpartners.ui.adminProfile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
    }

    private lateinit var iv_admin_profile_image: ImageView
    private lateinit var tv_admin_profile_name: TextView
    private lateinit var tv_admin_profile_email: TextView
    private lateinit var btn_admin_profile_edit: Button
    private lateinit var btn_admin_profile_add_points: Button
    private lateinit var edt_admin_profile_email_or_phone: EditText
    private lateinit var iv_admin_profile_verification_qr_code: ImageView

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

        iv_admin_profile_image = view.findViewById(R.id.iv_admin_profile_image)
        tv_admin_profile_name = view.findViewById(R.id.tv_admin_profile_name)
        tv_admin_profile_email = view.findViewById(R.id.tv_admin_profile_email)
        btn_admin_profile_edit = view.findViewById(R.id.btn_admin_profile_edit)
        iv_admin_profile_verification_qr_code = view.findViewById(R.id.iv_admin_profile_verification_qr_code)
        btn_admin_profile_add_points = view.findViewById(R.id.btn_admin_profile_add_points)
        edt_admin_profile_email_or_phone = view.findViewById(R.id.edt_admin_profile_email_or_phone)

        onAssumeUserPartnerData()

        btn_admin_profile_edit.setOnClickListener {
            val activity = activity as PinAdminActivity
            activity.goToEditProfileActivity()
        }

        btn_admin_profile_add_points.setOnClickListener {
            onAddPointsToUser()
        }

        return view
    }

    private fun onAddPointsToUser() {
        edt_admin_profile_email_or_phone.error = null
        val emailOrPhone = edt_admin_profile_email_or_phone.text.toString()

        if (emailOrPhone.isBlank()) {
            edt_admin_profile_email_or_phone.error = context!!.resources.getString(R.string.enter_email_or_phone)
            return
        }
        customProgressBar.show()
        when {
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches() -> viewModel.getUserByEmail(emailOrPhone)
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
                edt_admin_profile_email_or_phone.error = context!!.resources.getString(R.string.enter_correct_email_or_phone)
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

        if (!imageUrl.isNullOrBlank())
            Picasso.get().load(imageUrl).transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(
                iv_admin_profile_image
            )
        if (!adminName.isNullOrBlank())
            tv_admin_profile_name.text = adminName
        if (!adminEmail.isNullOrBlank())
            tv_admin_profile_email.text = adminEmail
        if (!pinId.isNullOrBlank())
            viewModel.getPinById(pinId!!)

        viewModel.pinMutableLiveData.observe(this, Observer {
            val verificationQrCode = it.verificationCode
            wasteType = it.wasteId!!
            if (!verificationQrCode.isNullOrBlank())
                onViewQRCode(verificationQrCode)
        })
    }


    private fun onViewQRCode(verificationQrCode: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(verificationQrCode, BarcodeFormat.QR_CODE, 800, 800)
            iv_admin_profile_verification_qr_code.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.userMutableLiveData.removeObservers(this)
    }

}
