package kz.nextstep.tazalykpartners.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.login.LoginActivity
import kz.nextstep.tazalykpartners.utils.TypefaceUtil

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
