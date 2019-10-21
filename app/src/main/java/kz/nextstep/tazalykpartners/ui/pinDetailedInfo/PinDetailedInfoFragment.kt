package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.PinDetailedInfoFragmentBinding
import kz.nextstep.tazalykpartners.utils.DividerItemDecorator

class PinDetailedInfoFragment : Fragment() {

    companion object {
        fun newInstance() = PinDetailedInfoFragment()
    }

    private lateinit var binding: PinDetailedInfoFragmentBinding
    private lateinit var viewModel: PinDetailedInfoViewModel

    lateinit var wasteIdArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pin_detailed_info_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(PinDetailedInfoViewModel::class.java)

        wasteIdArray = context?.resources?.getStringArray(R.array.waste_id)!!

        val bundle: Bundle? = arguments
        if (bundle != null) {
            initWorkingTimeView()
            initWasteTypeView()
            val pinId = bundle.getString(AppConstants.PIN_ID)
            if (pinId != null && pinId != "")
                viewModel.getPinById(pinId, wasteIdArray)
            onSetViewPager()
        }

        binding.viewModel = viewModel
        return binding.root
    }

    private fun initWorkingTimeView() {
        binding.rvPinDetailedInfoWorkingTimeSchedule.setHasFixedSize(true)
        binding.rvPinDetailedInfoWorkingTimeSchedule.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initWasteTypeView() {
        binding.rvPinDetailedInfoTakeType.layoutManager = LinearLayoutManager(activity)
        if (context != null) {
            val itemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(context!!, R.drawable.background_divider_item)!!)
            binding.rvPinDetailedInfoTakeType.addItemDecoration(itemDecoration)
        }
     }

    private fun onSetViewPager() {
        viewModel.pinImageAdapterLiveData.observe(this, Observer {
            if (it)
                binding.indicator.setViewPager(binding.vpPinDetailedInfoPager)
        })

    }

}
