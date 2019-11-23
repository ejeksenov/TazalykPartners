package kz.nextstep.tazalykpartners.ui.userInteractivity

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import kz.nextstep.domain.utils.AppConstants.REQUEST_CODE
import kz.nextstep.domain.utils.ChangeDateFormat
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.filterDateDays
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedDates
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedFilterType
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedWasteId

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.UserInteractivityFragmentBinding
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class UserInteractivityFragment : Fragment() {

    companion object {
        fun newInstance() = UserInteractivityFragment()
    }

    private lateinit var viewModel: UserInteractivityViewModel
    private lateinit var binding: UserInteractivityFragmentBinding
    private lateinit var customProgressBar: CustomProgressBar

    var selectedfilterDateType = "За месяц"
    var selectedfilterDateDays = 30
    var selectedFilterDates = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.user_interactivity_fragment,
            container,
            false
        )

        onAssignData()
        if (selectedFilterDates.isBlank())
            selectedFilterDates = ChangeDateFormat.onGetFilterDate(filterDateDays)

        viewModel = ViewModelProviders.of(this).get(UserInteractivityViewModel::class.java)
        customProgressBar = CustomProgressBar(context!!)

        binding.tvUserInteractivityDateFilterType.text = selectedFilterType

        binding.rvUserInteractivityList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        binding.rvUserInteractivityList.layoutManager = LinearLayoutManager(context)

        viewModel.getPassedUserList(selectedWasteId, selectedFilterDates)
        customProgressBar.show()

        viewModel.customProgressBarLiveData.observe(this, Observer {
            customProgressBar.dismiss()
        })

        binding.tvUserInteractivityDateFilterType.setOnClickListener {
            val intent = Intent(activity, FilterByDateActivity::class.java)
            intent.putExtra(AppConstants.SELECTED_DATES, selectedFilterDates)
            intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, selectedfilterDateType)
            activity?.startActivityForResult(intent, REQUEST_CODE)
        }

        binding.viewModel = viewModel

        return binding.root
    }

    private fun onAssignData() {
        selectedfilterDateType = selectedFilterType
        selectedFilterDates = selectedDates
        selectedfilterDateDays = filterDateDays
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                selectedDates = data.getStringExtra(AppConstants.SELECTED_DATES)!!
                selectedFilterType = data.getStringExtra(AppConstants.SELECTED_FILTER_TYPE)!!
                filterDateDays = data.getIntExtra(AppConstants.FILTER_DATE_DAYS, 30)
                binding.tvUserInteractivityDateFilterType.text = selectedFilterType
                onAssignData()
            }
            viewModel.getPassedUserList(selectedWasteId, selectedFilterDates)
        }
    }

}
