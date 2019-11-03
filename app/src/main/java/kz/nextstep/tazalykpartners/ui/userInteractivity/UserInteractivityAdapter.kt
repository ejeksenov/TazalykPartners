package kz.nextstep.tazalykpartners.ui.userInteractivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CircleTransform
import kz.nextstep.tazalykpartners.utils.data.PassedUserPinItem
import java.text.SimpleDateFormat
import java.util.*

class UserInteractivityAdapter: RecyclerView.Adapter<UserInteractivityAdapter.UserInteractivityViewHolder>() {

    lateinit var passedUserItemList: MutableList<PassedUserPinItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInteractivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_user_interactivity_item_layout, parent, false)
        return UserInteractivityViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (::passedUserItemList.isInitialized) passedUserItemList.size else 0
    }

    fun updatePassedUserPinList(passedUserItemList: MutableList<PassedUserPinItem>) {
        passedUserItemList.sortByDescending { SimpleDateFormat(AppConstants.DATE_FORMAT, Locale("ru")).parse(it.passedDate!!) }
        this.passedUserItemList = passedUserItemList
        notifyDataSetChanged()
    }

    fun clearAllList() {
        passedUserItemList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserInteractivityViewHolder, position: Int) {
        val passedUserPinItem = passedUserItemList[position]

        val imageUrl = passedUserPinItem.userImageUrl
        if (!imageUrl.isNullOrBlank())
            Picasso.get().load(imageUrl).transform(CircleTransform()).placeholder(R.drawable.user_placeholder_image).into(holder.ivRowUserInteractivityProfileLogo)
        else
            holder.ivRowUserInteractivityProfileLogo.setImageResource(R.drawable.user_placeholder_image)

        holder.tvRowUserInteractivityCompanyAddress.text = passedUserPinItem.pinAddress
        holder.tvRowUserInteractivityDate.text = passedUserPinItem.passedDate
        holder.tvRowUserInteractivityName.text = passedUserPinItem.userName

        val totalArr = passedUserPinItem.passedTotal?.split(";")!!
        var totalStr = ""
        var total = 0.0
        for ((index,item) in totalArr.withIndex()) {
            val itemArr = item.split(",")
            if (itemArr.size >= 3) {
                totalStr += "${itemArr[1]} ${itemArr[2]} кг " + if (index < totalArr.size - 1) "\n" else ""
                total += itemArr[2].toDouble()
            }
        }
        holder.tvRowUserInteractivityTotal.text = totalStr
        holder.tvRowUserInteractivityTotalSum.text = "Итого: $total кг"
    }

    class UserInteractivityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivRowUserInteractivityProfileLogo: ImageView = itemView.findViewById(R.id.iv_row_user_interactivity_profile_logo)
        val tvRowUserInteractivityName: TextView = itemView.findViewById(R.id.tv_row_user_interactivity_name)
        val tvRowUserInteractivityCompanyAddress: TextView = itemView.findViewById(R.id.tv_row_user_interactivity_company_address)
        val tvRowUserInteractivityTotal: TextView = itemView.findViewById(R.id.tv_row_user_interactivity_total)
        val tvRowUserInteractivityTotalSum: TextView = itemView.findViewById(R.id.tv_row_user_interactivity_total_sum)
        val tvRowUserInteractivityDate: TextView = itemView.findViewById(R.id.tv_row_user_interactivity_date)
    }
}