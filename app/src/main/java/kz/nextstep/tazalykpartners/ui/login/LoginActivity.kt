package kz.nextstep.tazalykpartners.ui.login

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    lateinit var customProgressBar: CustomProgressBar

    private var edtLoginEmail: EditText? = null
    private var edtLoginPassword: EditText? = null
    private var btnLoginSignIn: Button? = null
    private var btnLoginForgotPassword: Button? = null
    private var layoutLoginPassword: TextInputLayout? = null
    private var layoutLoginEmail: TextInputLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        if (viewModel.getCurrentUser()) {
            viewModel.getUserRole()
        } else{
            setContentView(R.layout.activity_login)


            customProgressBar = CustomProgressBar(this)

            edtLoginEmail = findViewById(R.id.edt_login_email)
            edtLoginPassword = findViewById(R.id.edt_login_password)
            btnLoginSignIn = findViewById(R.id.btn_login_sign_in)
            btnLoginForgotPassword = findViewById(R.id.btn_login_forgot_password)
            layoutLoginPassword = findViewById(R.id.layout_login_password)
            layoutLoginEmail = findViewById(R.id.layout_login_email)

            btnLoginSignIn?.setOnClickListener {
                onStartSignIn()
            }

            edtLoginEmail?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    layoutLoginEmail?.isErrorEnabled = false
                }

            })

            edtLoginPassword?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    layoutLoginPassword?.isErrorEnabled = false
                }

            })

            btnLoginForgotPassword?.setOnClickListener {
                val emailStr = edtLoginEmail?.text.toString()
                if (emailStr == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches())
                    Toast.makeText(this, resources.getText(R.string.correct_login_email), Toast.LENGTH_SHORT).show()
                else
                    sendResetPasswordAlertDialog(emailStr)
            }

            edtLoginPassword?.setOnEditorActionListener { p0, actionId, p2 ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onStartSignIn()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }


            viewModel.signInResultLiveData.observe(this, Observer {
                customProgressBar.dismiss()
                when (it) {
                    AppConstants.SUCCESS_PIN_ADMIN -> {
                        startPinAdminActivity()
                    }
                    AppConstants.SUCCESS_PIN_DIRECTOR -> {
                        startPinDirectorActivity()
                    }
                    AppConstants.SUCCESS_PRODUCT_SPONSOR -> {
                        startProductSponsorActivity()
                    }
                    AppConstants.ERROR_USER_NOT_FOUND ->
                        layoutLoginEmail?.error = it.toString()
                    else -> {
                        layoutLoginPassword?.error = it.toString()
                    }
                }
            })

        }

        viewModel.userRoleLiveData.observe(this, Observer {
            when(it) {
                AppConstants.SUCCESS_PIN_ADMIN -> {
                    startPinAdminActivity()
                }
                AppConstants.SUCCESS_PIN_DIRECTOR -> {
                    startPinDirectorActivity()
                }
                AppConstants.SUCCESS_PRODUCT_SPONSOR -> {
                    startProductSponsorActivity()
                }
                else -> {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }

            }
        })

    }

    private fun startProductSponsorActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun startPinDirectorActivity() {
        val intent = Intent(this, NavigationDrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun startPinAdminActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun sendResetPasswordAlertDialog(emailStr: String) {
        val message = "На вашу почту <br> <b>$emailStr</b> будет отправлено сообщение для создания нового пароля"
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(Html.fromHtml(message))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            resources.getText(R.string.login_send)
        ) { dialogInterface, _ ->
            viewModel.sendResetPasswordEmail(emailStr)
            dialogInterface.dismiss()
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            resources.getText(R.string.login_cancel)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    private fun onStartSignIn() {
        customProgressBar.show()
        val emailStr = edtLoginEmail?.text.toString()
        val passwordStr = edtLoginPassword?.text.toString()
        if (checkEmailAndPassword(emailStr, passwordStr))
            viewModel.signIn(emailStr, passwordStr)
        else
            customProgressBar.dismiss()
    }

    private fun checkEmailAndPassword(emailStr: String, passwordStr: String): Boolean {
        if (emailStr == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            edtLoginEmail?.requestFocus()
            layoutLoginEmail?.error = resources.getString(R.string.correct_login_email)
            return false
        } else if (passwordStr == "" || passwordStr.length < 6) {
            layoutLoginPassword?.requestFocus()
            return false
        }
        return true
    }

}
