package kz.nextstep.tazalykpartners.ui.pinlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.model.Pin
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.RowPinListItemBinding

class PinListAdapter: RecyclerView.Adapter<PinListAdapter.PinViewHolder>() {

    companion object {
        var onStatisticsBtnClick: ((String) -> Unit)? = null
    }

    private lateinit var pinListHashMap: HashMap<String,Pin>
    private var pinList: MutableList<Pin> = ArrayList()
    private var pinIdList: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        val binding: RowPinListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_pin_list_item, parent, false)
        return PinViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pinIdList.size
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.bind(pinIdList[position], pinList[position])
        holder.binding.ibRowPinListItemStatistics.setOnClickListener {
            onStatisticsBtnClick?.invoke(pinIdList[position])
        }
    }

    fun updatePinList(pinListHashMap: HashMap<String,Pin>) {
        this.pinListHashMap = pinListHashMap

        pinIdList.clear()
        pinList.clear()

        for (key in pinListHashMap.keys) {
            pinIdList.add(key)
            pinListHashMap[key]?.let { pinList.add(it) }
        }

        notifyDataSetChanged()
    }



    var onItemClick: ((String) -> Unit)? = null

    inner class PinViewHolder(val binding: RowPinListItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val viewModel = PinViewModel()
        fun bind(pinId: String, pin: Pin) {
            viewModel.bind(pin, pinId)
            binding.viewModel = viewModel
            binding.root.setOnClickListener{
                onItemClick?.invoke(pinId)
            }
        }
    }
}