package kz.nextstep.tazalykpartners.ui.addEditPin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kz.nextstep.tazalykpartners.R

class PinPhotosAdapter(val pinPhotoList: MutableList<Any>): RecyclerView.Adapter<PinPhotosAdapter.PinPhotosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinPhotosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_pin_photos_item_layout, parent, false)
        return PinPhotosViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (pinPhotoList.isEmpty()) 0 else pinPhotoList.size
    }

    override fun onBindViewHolder(holder: PinPhotosViewHolder, position: Int) {
        val image = pinPhotoList[position]
        when(image) {
            is Uri -> holder.iv_pin_photo.setImageURI(image)
            is String -> if (image.isNotBlank()) Picasso.get().load(image).fit().centerCrop().placeholder(R.drawable.loading_image).into(holder.iv_pin_photo)
        }

        holder.btn_pin_photo_delete.setOnClickListener {
            onItemClick!!.invoke(image, position, false)
        }

        holder.iv_pin_photo.setOnClickListener {
            onItemClick!!.invoke(image, position, true)
        }
    }

    var onItemClick: ((Any, Int, Boolean) -> Unit)? = null


    class PinPhotosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val iv_pin_photo: ImageView = itemView.findViewById(R.id.iv_pin_photo)
        val btn_pin_photo_delete: ImageButton = itemView.findViewById(R.id.btn_pin_photo_delete)
    }
}