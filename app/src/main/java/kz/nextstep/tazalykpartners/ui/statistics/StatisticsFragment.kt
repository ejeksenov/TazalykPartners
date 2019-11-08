package kz.nextstep.tazalykpartners.ui.statistics

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.AppConstants.waste_type_blago
import kz.nextstep.domain.utils.AppConstants.waste_type_recycle
import kz.nextstep.domain.utils.AppConstants.waste_type_utilization
import kz.nextstep.domain.utils.ChangeDateFormat

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.StatisticsFragmentBinding
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.filterDateDays
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedDates
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedFilterType
import kz.nextstep.tazalykpartners.ui.passedUserList.StatisticsPassedUserListActivity
import kz.nextstep.tazalykpartners.utils.CustomProgressBar


class StatisticsFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticsFragment()

    }

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var binding: StatisticsFragmentBinding
    private lateinit var customProgressBar: CustomProgressBar


    private var selectedWasteType = waste_type_recycle
    var selectedfilterDateType = "За месяц"
    var selectedfilterDateDays = 30
    var selectedFilterDates = ""
    private var cnt = 1

    private var pinId: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.statistics_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(StatisticsViewModel::class.java)


        binding.rvStatisticsHistoryList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        binding.rvStatisticsHistoryList.layoutManager = LinearLayoutManager(context)

        val bundle: Bundle? = arguments
        if (bundle != null) {
            pinId = bundle.getString(AppConstants.PIN_ID)
            if (!pinId.isNullOrBlank()) {
                binding.tvStatisticsFilterPeriod.text = selectedFilterType

                onAssignData()

                if (selectedFilterDates.isBlank())
                    selectedFilterDates = ChangeDateFormat.onGetFilterDate(filterDateDays)

                viewModel.getHistoryPinList(pinId!!, selectedfilterDateDays, selectedFilterDates, selectedWasteType)
                customProgressBar = CustomProgressBar(context!!)
                customProgressBar.show()

                viewModel.customProgressBarLiveData.observe(this, Observer {
                    customProgressBar.dismiss()
                })

                binding.tvStatisticsPassedWasteType.setOnClickListener {
                    onManageTypeOfWasteFilter()
                }

                viewModel.statisticsHistoryAdapter.onItemClick = {
                    val intent = Intent(activity, StatisticsPassedUserListActivity::class.java)
                    intent.putParcelableArrayListExtra(
                        AppConstants.PASSED_USER_LIST,
                        ArrayList<Parcelable>(it)
                    )
                    startActivity(intent)
                }
            }
        }

        binding.viewModel = viewModel
        return binding.root
    }

    private fun onAssignData() {
        selectedfilterDateType = selectedFilterType
        selectedFilterDates = selectedDates
        selectedfilterDateDays = filterDateDays
    }

    private fun onManageTypeOfWasteFilter() {
        cnt++
        if (cnt == 4)
            cnt = 1
        when (cnt) {
            1 -> {
                binding.tvStatisticsPassedWasteType.text = resources.getString(R.string.recycle)
                selectedWasteType = waste_type_recycle
            }
            2 -> {
                binding.tvStatisticsPassedWasteType.text = resources.getString(R.string.utilization)
                selectedWasteType = waste_type_utilization
            }
            3 -> {
                binding.tvStatisticsPassedWasteType.text = resources.getString(R.string.blago)
                selectedWasteType = waste_type_blago
            }
        }
        viewModel.getHistoryPinList(pinId!!, filterDateDays, selectedDates, selectedWasteType)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedDates = data.getStringExtra(AppConstants.SELECTED_DATES)!!
            selectedFilterType = data.getStringExtra(AppConstants.SELECTED_FILTER_TYPE)!!
            filterDateDays = data.getIntExtra(AppConstants.FILTER_DATE_DAYS, 30)
            binding.tvStatisticsFilterPeriod.text = selectedFilterType
            onAssignData()
            viewModel.getHistoryPinList(pinId!!, selectedfilterDateDays, selectedFilterDates, selectedWasteType)
        }
    }


}
