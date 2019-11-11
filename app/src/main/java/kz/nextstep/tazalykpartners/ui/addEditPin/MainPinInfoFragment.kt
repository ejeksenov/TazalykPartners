package kz.nextstep.tazalykpartners.ui.addEditPin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.sapereaude.maskedEditText.MaskedEditText
import kz.nextstep.domain.model.Pin

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pin


class MainPinInfoFragment : Fragment() {

    companion object {
        fun newInstance() = MainPinInfoFragment()
    }

    lateinit var edtMainPinInfoName: EditText
    lateinit var edtMainPinInfoAdminName: EditText
    lateinit var edtMainPinInfoAdminPhone: MaskedEditText
    lateinit var edtMainPinInfoNotice: EditText
    lateinit var btnMainPinInfoLocation: Button
    lateinit var btnMainPinInfoWorkingTime: Button
    lateinit var btnMainPinInfoWasteId: Button
    lateinit var btnMainPinInfoImage: Button
    lateinit var btnMainPinInfoSave: Button
    lateinit var spMainPinInfoAdminRole: Spinner

    var pinName = ""
    var pinAdminName = ""
    var pinAdminPhone = ""
    var pinNotice = ""

    var rolesID = arrayOf("k", "t", "b")
    var roles = arrayOf("KWR","Tazalyk", "Другие")
    var role = ""

    lateinit var mPin: Pin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_pin_info, container, false)

        edtMainPinInfoName = view.findViewById(R.id.edt_main_pin_info_name)
        edtMainPinInfoAdminName = view.findViewById(R.id.edt_main_pin_info_admin_name)
        edtMainPinInfoAdminPhone = view.findViewById(R.id.edt_main_pin_info_admin_phone)
        edtMainPinInfoNotice = view.findViewById(R.id.edt_main_pin_info_notice)
        btnMainPinInfoLocation = view.findViewById(R.id.btn_main_pin_info_location)
        btnMainPinInfoWorkingTime = view.findViewById(R.id.btn_main_pin_info_working_time)
        btnMainPinInfoWasteId = view.findViewById(R.id.btn_main_pin_info_waste_id)
        btnMainPinInfoImage = view.findViewById(R.id.btn_main_pin_info_image)
        btnMainPinInfoSave = view.findViewById(R.id.btn_main_pin_info_save)
        spMainPinInfoAdminRole = view.findViewById(R.id.sp_main_pin_info_admin_role)

        if (activity != null) {
            val addEditPinActivity = (activity as AddEditPinActivity)
            btnMainPinInfoWorkingTime.setOnClickListener {
                addEditPinActivity.goToPinWorkTimeFragment()
            }

            btnMainPinInfoImage.setOnClickListener {
                addEditPinActivity.goToPinPhotosFragment()
            }

            btnMainPinInfoLocation.setOnClickListener {
                addEditPinActivity.goToMapPinFragment()
            }

            btnMainPinInfoWasteId.setOnClickListener {
                addEditPinActivity.goToFilterByType()
            }

            btnMainPinInfoSave.setOnClickListener {
                if (onCheckFields())
                    addEditPinActivity.onSaveData()
            }
        }

        mPin = pin

        edtMainPinInfoName.setText(mPin.name)
        edtMainPinInfoAdminName.setText(mPin.phoneName)
        edtMainPinInfoNotice.setText(mPin.description)

        pinAdminPhone = if (mPin.phone!!.contains("+7")) mPin.phone!!.replace("+7", "") else mPin.phone!!
        edtMainPinInfoAdminPhone.setText(pinAdminPhone)

        onSetRoleAdapter()

        return view
    }

    private fun onSetRoleAdapter() {
        val roleAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMainPinInfoAdminRole.adapter = roleAdapter
        var selectionIndex = 0
        if (!mPin.partner.isNullOrBlank()) {
            selectionIndex = rolesID.indexOf(mPin.partner)
        }
        spMainPinInfoAdminRole.setSelection(selectionIndex)
        spMainPinInfoAdminRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                role = rolesID[position]
            }

        }
    }

    private fun onCheckFields(): Boolean {
        pinName = edtMainPinInfoAdminName.text.toString()
        pinAdminPhone = edtMainPinInfoAdminPhone.rawText
        if (pinAdminPhone.isNotBlank()) pinAdminPhone = "+7$pinAdminPhone"
        pinAdminName = edtMainPinInfoAdminName.text.toString()
        pinNotice = edtMainPinInfoNotice.text.toString()
        if (pinName.isBlank()) {
            edtMainPinInfoName.error = resources.getString(R.string.enter_pin_name)
            return false
        }
        if (pinAdminPhone.isBlank()) {
            edtMainPinInfoAdminPhone.error = resources.getString(R.string.enter_admin_phone)
            return false
        }
        if (pinAdminName.isBlank()) {
            edtMainPinInfoAdminName.error = resources.getString(R.string.enter_admin_name)
            return false
        }

        pin.name = pinName
        pin.phone = pinAdminPhone
        pin.phoneName = pinAdminName
        pin.description = pinNotice

        return true
    }


}
