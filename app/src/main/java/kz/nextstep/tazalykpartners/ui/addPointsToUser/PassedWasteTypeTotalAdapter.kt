package kz.nextstep.tazalykpartners.ui.addPointsToUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R

class PassedWasteTypeTotalAdapter(val wasteItemTotalList: MutableList<String>): RecyclerView.Adapter<PassedWasteTypeTotalAdapter.PassedWasteTypeTotalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassedWasteTypeTotalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_passed_waste_type_total_layout, parent, false)
        return PassedWasteTypeTotalViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wasteItemTotalList.size
    }

    override fun onBindViewHolder(holder: PassedWasteTypeTotalViewHolder, position: Int) {
        val wasteItemTotal = wasteItemTotalList[position]
        val wasteItemArr = wasteItemTotal.split(",")
        if (wasteItemArr.size >= 4) {
            holder.tv_passed_waste_type.text = wasteItemArr[1]
            val totalStr = "${wasteItemArr[3]} кг"
            holder.tv_passed_waste_type_total.text = totalStr
        }
        holder.btn_passed_waste_type_total_delete.setOnClickListener {
            onChangeOrDeleteItem!!.invoke(wasteItemTotal)
        }
    }

    var onChangeOrDeleteItem:((String) -> Unit)? = null


    class PassedWasteTypeTotalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv_passed_waste_type: TextView = itemView.findViewById(R.id.tv_passed_waste_type)
        val tv_passed_waste_type_total: TextView = itemView.findViewById(R.id.tv_passed_waste_type_total)
        val btn_passed_waste_type_total_delete: ImageButton = itemView.findViewById(R.id.btn_passed_waste_type_total_delete)
    }
}