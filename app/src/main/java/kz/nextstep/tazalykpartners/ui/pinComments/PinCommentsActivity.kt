package kz.nextstep.tazalykpartners.ui.pinComments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.ActivityPinCommentsBinding
import kz.nextstep.tazalykpartners.ui.pinDetailedInfo.PinDetailedInfoViewModel

class PinCommentsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    var pinId = ""

    private lateinit var binding: ActivityPinCommentsBinding
    private lateinit var viewModel: PinCommentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_pin_comments)

        viewModel = ViewModelProviders.of(this).get(PinCommentsViewModel::class.java)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.rvPinCommentsList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        binding.rvPinCommentsList.layoutManager = LinearLayoutManager(this)

        if (intent != null) {
            pinId = intent.getStringExtra(AppConstants.PIN_ID)!!
            if (pinId.isNotEmpty() && pinId.isNotBlank()) {
                viewModel.getComments(pinId)
            }
        }

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        binding.viewModel = viewModel
    }
}
