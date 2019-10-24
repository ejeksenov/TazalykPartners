package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.RowTakeTypeItemLayoutBinding
import kz.nextstep.tazalykpartners.utils.data.TakeType

class PinTakeTypeAdapter : RecyclerView.Adapter<PinTakeTypeAdapter.PinTakeTypeViewHolder>() {

    lateinit var takeTypeList: MutableList<TakeType>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinTakeTypeViewHolder {
        val binding: RowTakeTypeItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_take_type_item_layout,
            parent,
            false
        )
        return PinTakeTypeViewHolder(binding)
    }

    fun updateList(takeTypeList: MutableList<TakeType>) {
        this.takeTypeList = takeTypeList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::takeTypeList.isInitialized) takeTypeList.size else 0
    }

    override fun onBindViewHolder(holder: PinTakeTypeViewHolder, position: Int) {
        holder.bind(takeTypeList[position])
        holder.binding.rvRowTakeTypeItemMarking.setHasFixedSize(true)
        holder.binding.rvRowTakeTypeItemMarking.layoutManager = LinearLayoutManager(
            holder.binding.root.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    class PinTakeTypeViewHolder(val binding: RowTakeTypeItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewModel = PinTakeTypeViewModel()
        fun bind(takeType: TakeType) {
            viewModel.bind(takeType)
            binding.viewModel = viewModel
        }
    }
}