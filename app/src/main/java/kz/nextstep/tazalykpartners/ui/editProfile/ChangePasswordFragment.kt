package kz.nextstep.tazalykpartners.ui.editProfile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import kz.nextstep.tazalykpartners.MainApplication

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class ChangePasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var edtChangeUserPasswordOld: EditText
    private lateinit var edtChangeUserPasswordNew: EditText
    private lateinit var edtChangeUserPasswordConfirmNew: EditText
    private lateinit var btnChangeUserPassword: Button
    private lateinit var customProgressBar: CustomProgressBar

    var currentPassword = ""
    var newPassword = ""
    var confirmNewPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.change_password_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)

        customProgressBar = CustomProgressBar(context!!)

        edtChangeUserPasswordConfirmNew = view.findViewById(R.id.edt_change_user_password_confirm_new)
        edtChangeUserPasswordOld = view.findViewById(R.id.edt_change_user_password_old)
        edtChangeUserPasswordNew = view.findViewById(R.id.edt_change_user_password_new)
        btnChangeUserPassword = view.findViewById(R.id.btn_change_user_password)

        edtChangeUserPasswordOld.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                currentPassword = p0.toString()
                edtChangeUserPasswordOld.error = null
            }
        })

        edtChangeUserPasswordNew.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newPassword = p0.toString()
                edtChangeUserPasswordNew.error = null
            }
        })

        edtChangeUserPasswordConfirmNew.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                confirmNewPassword = p0.toString()
                edtChangeUserPasswordConfirmNew.error = null
            }
        })

        btnChangeUserPassword.setOnClickListener {
            if (onCheckInputFields()) {
                customProgressBar.show()
                viewModel.changeUserPartnerPassword(currentPassword, newPassword)
            }
        }

        viewModel.changePasswordLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (it) {
                Toast.makeText(context, resources.getString(R.string.success_change_password), Toast.LENGTH_SHORT)
                    .show()
                activity?.onBackPressed()
            } else
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_change_data),
                    Toast.LENGTH_SHORT
                ).show()
        })

        return view
    }

    private fun onCheckInputFields(): Boolean {
        when {
            currentPassword.isBlank() -> {
                edtChangeUserPasswordOld.error = resources.getString(R.string.current_password)
                edtChangeUserPasswordOld.requestFocus()
                return false
            }
            newPassword.isBlank() -> {
                edtChangeUserPasswordNew.error = resources.getString(R.string.new_password)
                edtChangeUserPasswordNew.requestFocus()
                return false
            }
            confirmNewPassword.isBlank() -> {
                edtChangeUserPasswordConfirmNew.error = resources.getString(R.string.confirm_new_password)
                edtChangeUserPasswordConfirmNew.requestFocus()
                return false
            }
            newPassword.length < 6 -> {
                edtChangeUserPasswordNew.error = resources.getString(R.string.passwords_less_than_six_characters)
                edtChangeUserPasswordNew.requestFocus()
                return false
            }
            confirmNewPassword != newPassword -> {
                edtChangeUserPasswordNew.error = resources.getString(R.string.passwords_not_match)
                edtChangeUserPasswordNew.requestFocus()
                return false
            }
            else -> return true
        }

    }


}
