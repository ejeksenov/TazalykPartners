package kz.nextstep.tazalykpartners.ui.passedUserList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.databinding.RowPassedUserItemLayoutBinding
import kz.nextstep.tazalykpartners.utils.data.PassedUserItem
import java.text.SimpleDateFormat
import java.util.*


class PassedUserListAdapter : RecyclerView.Adapter<PassedUserListAdapter.PassedUserViewHolder>() {

    lateinit var passedUserItemList: MutableList<PassedUserItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassedUserViewHolder {
        val binding: RowPassedUserItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_passed_user_item_layout,
            parent,
            false
        )
        return PassedUserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (::passedUserItemList.isInitialized) passedUserItemList.size else 0
    }

    fun updatePassedUserItemList(passedUserItemList: MutableList<PassedUserItem>) {
        passedUserItemList.sortByDescending { SimpleDateFormat("MMM dd, yyyy", Locale("ru")).parse(it.passedDate!!)  }
        this.passedUserItemList = passedUserItemList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PassedUserViewHolder, position: Int) {
        val passedUserItem = passedUserItemList[position]
        holder.bind(passedUserItem)
    }

    class PassedUserViewHolder(val binding: RowPassedUserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewModel = PassedUserItemViewModel()
        fun bind(passedUserItem: PassedUserItem) {
            viewModel.bind(passedUserItem)
            binding.viewModel = viewModel
        }
    }
}