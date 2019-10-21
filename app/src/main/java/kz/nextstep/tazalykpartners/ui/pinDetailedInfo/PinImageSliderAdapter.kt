package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import kz.nextstep.tazalykpartners.R

class PinImageSliderAdapter: PagerAdapter(){

    lateinit var imageUrlList: MutableList<String>

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (::imageUrlList.isInitialized) imageUrlList.size - 1 else 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun updatePinImage(imageUrlList: MutableList<String>) {
        this.imageUrlList = imageUrlList
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout = LayoutInflater.from(container.context).inflate(R.layout.row_pin_image_item, container, false)
        val imageUrl = imageUrlList[position]
        val image: ImageView = imageLayout.findViewById(R.id.image)
        if (imageUrl != "")
            Picasso.get().load(imageUrl).fit().centerCrop().placeholder(R.drawable.point_detailed_placeholder).into(image)
        container.addView(imageLayout, 0)
        return imageLayout
    }
}