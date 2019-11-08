package kz.nextstep.tazalykpartners.ui.addEditPin


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.nextstep.tazalykpartners.R

class PinWorkTimeFragment : Fragment() {

    companion object {
        fun newInstance() = PinWorkTimeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_work_time, container, false)
    }


}
