package kz.nextstep.tazalykpartners.ui.userInteractivity

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.ChangeDateFormat

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.UserInteractivityFragmentBinding
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.filterDateDays
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedDates
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedFilterType
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedWasteId
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class UserInteractivityFragment : Fragment() {

    companion object {
        fun newInstance() = UserInteractivityFragment()
    }

    private lateinit var viewModel: UserInteractivityViewModel
    private lateinit var binding: UserInteractivityFragmentBinding
    private lateinit var customProgressBar: CustomProgressBar

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

        selectedDates = ChangeDateFormat.onGetFilterDate(filterDateDays)

        viewModel = ViewModelProviders.of(this).get(UserInteractivityViewModel::class.java)
        customProgressBar = CustomProgressBar(context!!)

        binding.tvUserInteractivityDateFilterType.text = selectedFilterType

        binding.rvUserInteractivityList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        binding.rvUserInteractivityList.layoutManager = LinearLayoutManager(context)

        viewModel.getPassedUserList(selectedWasteId, selectedDates)

        viewModel.customProgressBarLiveData.observe(this, Observer {
            if (it)
                customProgressBar.dismiss()
            else
                customProgressBar.show()

        })

        binding.viewModel = viewModel

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null ) {
            if (requestCode == AppConstants.REQUEST_CODE) {
                selectedDates = data.getStringExtra(AppConstants.SELECTED_DATES)!!
                selectedFilterType = data.getStringExtra(AppConstants.SELECTED_FILTER_TYPE)!!
                filterDateDays = data.getIntExtra(AppConstants.FILTER_DATE_DAYS, 30)
                binding.tvUserInteractivityDateFilterType.text = selectedFilterType
            } else if (requestCode == 2) {
                selectedWasteId = data.getStringExtra(AppConstants.SELECTED_WASTE_ID)!!
            }
            viewModel.getPassedUserList(selectedWasteId, selectedDates)
        }
    }

}
