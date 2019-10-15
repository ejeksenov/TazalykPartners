package kz.nextstep.tazalykpartners.ui.pinlist

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.PinListFragmentBinding
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity

class PinListFragment : Fragment() {

    companion object {
        fun newInstance() = PinListFragment()
    }

    private lateinit var binding: PinListFragmentBinding
    private lateinit var viewModel: PinListViewModel

    private var filterTypeIds = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.pin_list_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(PinListViewModel::class.java)

        binding.rvPinList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvPinList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        viewModel.getUserPartnerPinIds(filterTypeIds)

        binding.viewModel = viewModel

        viewModel.pinListAdapter.onItemClick = {pinId ->
            //TODO go to detailed pin and put extra pinId
        }

        binding.fbPinListAddPin.setOnClickListener {
            val intent = Intent(activity, AddEditPinActivity::class.java)
            intent.putExtra(AppConstants.ADD_EDIT_PIN_DATA, "")
            startActivity(intent)
        }
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == AppConstants.REQUEST_CODE) {
            filterTypeIds = data?.getStringExtra(AppConstants.RESULT_FILTER_DATA).toString()
            viewModel.getUserPartnerPinIds(filterTypeIds)
        }
    }


}
