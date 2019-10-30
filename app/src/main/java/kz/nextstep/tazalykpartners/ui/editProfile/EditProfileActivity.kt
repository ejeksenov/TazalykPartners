package kz.nextstep.tazalykpartners.ui.editProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.SampleScreen
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class EditProfileActivity : AppCompatActivity() {

    companion object {
        val router = MainApplication.INSTANCE?.getRouter()!!
    }

    lateinit var toolbar: Toolbar

    private val navigator = SupportAppNavigator(this, R.id.layout_edit_profile_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        router.replaceScreen(SampleScreen(ChangeUserDataFragment.newInstance()))

        toolbar.setNavigationOnClickListener {
            manageOnBackPressed()
        }
    }

    override fun onBackPressed() {
        manageOnBackPressed()
    }

    private fun manageOnBackPressed() {
        toolbar.title = resources.getString(R.string.nav_edit_profile)
        super.onBackPressed()
    }


    fun goToChangeEmail() {
        toolbar.title = resources.getString(R.string.change_email)
        router.navigateTo(SampleScreen(ChangeEmailFragment.newInstance()))
    }

    fun goToChangePassword() {
        toolbar.title = resources.getString(R.string.change_password)
        router.navigateTo(SampleScreen(ChangePasswordFragment.newInstance()))
    }

    override fun onResume() {
        super.onResume()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }

}
