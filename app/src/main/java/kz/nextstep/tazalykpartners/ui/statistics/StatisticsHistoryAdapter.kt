package kz.nextstep.tazalykpartners.ui.statistics

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.RowStatisticsHistoryItemLayoutBinding
import kz.nextstep.tazalykpartners.utils.data.WasteItem
import kotlin.math.roundToInt

class StatisticsHistoryAdapter : RecyclerView.Adapter<StatisticsHistoryAdapter.StatisticsHistoryViewHolder>() {

    lateinit var wasteItemList: MutableList<WasteItem>
    lateinit var filteredWasteItemList: MutableList<WasteItem>

    var totalSum = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsHistoryViewHolder {
        val binding: RowStatisticsHistoryItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_statistics_history_item_layout,
            parent,
            false
        )
        return StatisticsHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (::filteredWasteItemList.isInitialized) filteredWasteItemList.size else 0
    }

    fun upDateStatisticsHistoryAdapter(
        wasteItemList: MutableList<WasteItem>,
        filteredWasteItemList: MutableList<WasteItem>
    ) {
        this.wasteItemList = wasteItemList
        this.filteredWasteItemList = filteredWasteItemList
        totalSum = filteredWasteItemList.sumByDouble { it.passed_total!! }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StatisticsHistoryViewHolder, position: Int) {
        val wasteItem = filteredWasteItemList[position]
        holder.bind(wasteItem, holder.binding.root.context)
        if (totalSum > 0.0) {
            val passedTotal: Double = wasteItem.passed_total ?: 0.0
            holder.binding.pbStatisticsHistoryItemProgress.progress = (passedTotal.times(100) / totalSum).roundToInt()
        }
    }


    class StatisticsHistoryViewHolder(val binding: RowStatisticsHistoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val statisticsTypeViewModel = StatisticsTypeViewModel()
        fun bind(wasteItem: WasteItem, context: Context) {
            statisticsTypeViewModel.bind(wasteItem, context)
            binding.viewModel = statisticsTypeViewModel
        }
    }
}