package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kz.nextstep.tazalykpartners.R

class PinTakeTypeMarkingAdapter: RecyclerView.Adapter<PinTakeTypeMarkingAdapter.PinTakeTypeMarkingViewHolder>() {

    private lateinit var markingImageUrlList: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinTakeTypeMarkingViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.row_take_type_marking_item_layout, parent, false)
        return PinTakeTypeMarkingViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return if (::markingImageUrlList.isInitialized) markingImageUrlList.size else 0
    }

    fun updateList(markingImageUrlListStr: String) {
        this.markingImageUrlList = markingImageUrlListStr.split(",").toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PinTakeTypeMarkingViewHolder, position: Int) {
        val imageUrl = markingImageUrlList[position]
        if (imageUrl != "")
            Picasso.get().load(imageUrl).fit().centerInside().placeholder(R.drawable.loading_image).into(holder.iv_row_take_type_marking_logo)
    }

    class PinTakeTypeMarkingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var iv_row_take_type_marking_logo: ImageView? = null
        init {
            iv_row_take_type_marking_logo = itemView.findViewById(R.id.iv_row_take_type_marking_logo)
        }
    }
}