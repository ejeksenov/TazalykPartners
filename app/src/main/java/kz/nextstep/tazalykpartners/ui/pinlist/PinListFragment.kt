package kz.nextstep.tazalykpartners.ui.pinlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.nextstep.tazalykpartners.R

class PinListFragment : Fragment() {

    companion object {
        fun newInstance() = PinListFragment()
    }

    private lateinit var viewModel: PinListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pin_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PinListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
