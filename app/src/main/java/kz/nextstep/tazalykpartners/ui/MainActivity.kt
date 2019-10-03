package kz.nextstep.tazalykpartners.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kz.nextstep.domain.model.Pin
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.base.ViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var addPinViewModel: AddPinViewModel
    lateinit var mainApplication: MainApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainApplication = application as MainApplication

       // mainApplication.getApplicationComponent().inject(this)

        addPinViewModel = ViewModelProviders.of(this, ViewModelFactory(mainApplication)).get(AddPinViewModel::class.java)

        addPinViewModel.bound()


        addPinViewModel.message.observe(this, Observer {
            anyText.text = it
        })

    }
}
