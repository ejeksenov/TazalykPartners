package kz.nextstep.tazalykpartners.ui.addEditPin


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.nextstep.tazalykpartners.R

class MapPinFragment : Fragment() {

    companion object {
        fun newInstance() = MapPinFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_pin, container, false)
    }


}
