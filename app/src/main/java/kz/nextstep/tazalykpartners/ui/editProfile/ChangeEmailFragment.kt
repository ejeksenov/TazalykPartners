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

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class ChangeEmailFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeEmailFragment()
    }

    private lateinit var viewModel: ChangeEmailViewModel
    private lateinit var edtChangeUserEmailNew: EditText
    private lateinit var edtChangeUserEmailPassword: EditText
    private lateinit var btnChangeUserEmail: Button
    private lateinit var customProgressBar: CustomProgressBar

    var password = ""
    var newEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.change_email_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(ChangeEmailViewModel::class.java)

        edtChangeUserEmailNew = view.findViewById(R.id.edt_change_user_email_new)
        edtChangeUserEmailPassword = view.findViewById(R.id.edt_change_user_email_password)
        btnChangeUserEmail = view.findViewById(R.id.btn_change_user_email)
        customProgressBar = CustomProgressBar(context!!)

        edtChangeUserEmailNew.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newEmail = p0.toString()
                edtChangeUserEmailNew.error = null
            }
        })

        edtChangeUserEmailPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = p0.toString()
                edtChangeUserEmailPassword.error = null
            }
        })

        btnChangeUserEmail.setOnClickListener {
            if (checkInputFields()) {
                customProgressBar.show()
                viewModel.changeEmail(newEmail, password)
            }
        }

        viewModel.changeEmailLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (it) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.success_change_email),
                    Toast.LENGTH_SHORT
                ).show()
                activity?.onBackPressed()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_change_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        return view
    }

    private fun checkInputFields(): Boolean {
        if (newEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            edtChangeUserEmailNew.error = resources.getString(R.string.correct_login_email)
            edtChangeUserEmailNew.requestFocus()
            return false
        } else if (password.isBlank() || password.length < 6) {
            edtChangeUserEmailPassword.error = resources.getString(R.string.passwords_less_than_six_characters)
            edtChangeUserEmailPassword.requestFocus()
            return false
        }
        return true
    }


}
