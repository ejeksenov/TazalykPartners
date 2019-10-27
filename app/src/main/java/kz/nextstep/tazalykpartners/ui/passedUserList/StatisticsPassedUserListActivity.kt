package kz.nextstep.tazalykpartners.ui.passedUserList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.ActivityStatisticsPassedUserListBinding
import kz.nextstep.tazalykpartners.utils.data.WasteItem

class StatisticsPassedUserListActivity : AppCompatActivity() {

    lateinit var binding: ActivityStatisticsPassedUserListBinding
    lateinit var statisticsPassedUserListViewModel: StatisticsPassedUserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistics_passed_user_list)
        statisticsPassedUserListViewModel =
            ViewModelProviders.of(this).get(StatisticsPassedUserListViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent != null) {
            val list: ArrayList<WasteItem> =
                intent.getParcelableArrayListExtra(AppConstants.PASSED_USER_LIST)!!
            if (!list.isNullOrEmpty()) {
                binding.rvStatisticsPassedUserList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
                binding.rvStatisticsPassedUserList.layoutManager = LinearLayoutManager(this)

                onSetToolbarTitle(list[0].selected_waste_id)

                statisticsPassedUserListViewModel.getPassedUserListData(list)
            }
        }



        binding.toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        binding.viewModel = statisticsPassedUserListViewModel
    }

    private fun onSetToolbarTitle(selectedWasteId: String?) {
        supportActionBar?.title = selectedWasteId?.capitalize()
    }
}
