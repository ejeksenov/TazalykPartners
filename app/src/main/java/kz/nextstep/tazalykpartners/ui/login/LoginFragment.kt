package kz.nextstep.tazalykpartners.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kz.nextstep.tazalykpartners.MainApplication

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.LoginFragmentBinding
import kz.nextstep.tazalykpartners.ui.SampleScreen
import kz.nextstep.tazalykpartners.ui.pinlist.PinListFragment
import kz.nextstep.tazalykpartners.utils.CustomProgressBar
import ru.terrakok.cicerone.Router

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel
    lateinit var router: Router

    lateinit var customProgressBar: CustomProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        router = MainApplication.INSTANCE?.getRouter()!!

        customProgressBar = CustomProgressBar(context!!)

        binding.btnLoginSignIn.setOnClickListener {
            onStartSignIn()
        }

        binding.edtLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.layoutLoginEmail.isErrorEnabled = false
            }

        })

        binding.edtLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.layoutLoginPassword.isErrorEnabled = false
            }

        })

        binding.btnLoginForgotPassword.setOnClickListener {
            val emailStr = binding.edtLoginEmail.text?.toString()
            if (emailStr == null || emailStr == "")
                Toast.makeText(context, resources.getText(R.string.enter_login_email), Toast.LENGTH_SHORT).show()
            else
                sendResetPasswordAlertDialog(emailStr)
        }

        binding.edtLoginPassword.setOnEditorActionListener { p0, actionId, p2 ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onStartSignIn()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


        viewModel.signInResultLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            when (it) {
                is Boolean -> {
                    if (it) {
                        router.replaceScreen(SampleScreen(PinListFragment::class.java.name))
                    }
                }
                is Int -> {
                    binding.layoutLoginPassword.error = resources.getString(it)
                }
                is String -> {
                    binding.layoutLoginPassword.error = it
                }
            }
        })
    }

    private fun sendResetPasswordAlertDialog(emailStr: String) {
        val message = "На вашу почту $emailStr будет отправлено сообщение для создания нового пароля"
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Отправить") { dialogInterface, i ->
            viewModel.sendResetPasswordEmail(emailStr)
            dialogInterface.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    private fun onStartSignIn() {
        customProgressBar.show()
        val emailStr = binding.edtLoginEmail.text?.toString()
        val passwordStr = binding.edtLoginPassword.text?.toString()
        if (checkEmailAndPassword(emailStr!!, passwordStr!!)) {
            viewModel.signIn(emailStr, passwordStr)
        } else
            customProgressBar.dismiss()
    }

    private fun checkEmailAndPassword(emailStr: String, passwordStr: String): Boolean {
        if (emailStr == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            binding.edtLoginEmail.requestFocus()
            binding.layoutLoginEmail.error = resources.getString(R.string.correctEmail)
            return false
        } else if (passwordStr == "" || passwordStr.length < 6) {
            binding.layoutLoginPassword.requestFocus()
            return false
        }
        return true
    }


}
