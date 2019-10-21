package kz.nextstep.tazalykpartners.utils

import android.graphics.Typeface
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import kz.nextstep.tazalykpartners.utils.extension.getParentActivity
import kz.nextstep.tazalykpartners.R

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("averageRatingText")
fun setAverageRatingText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            if (value == "0.0" || value == "0") {
                view.text = parentActivity.resources.getText(R.string.yet_no_comment)
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                view.setTypeface(null, Typeface.ITALIC)
            } else {
                view.text = value
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_yellow_24dp, 0)
                view.setTypeface(null, Typeface.NORMAL)
            }
        })
    }
}

@BindingAdapter("mutableHtmlText")
fun setMutableHtmlText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = Html.fromHtml(value) ?: "" })
    }
}

@BindingAdapter("workingScheduleText")
fun setWorkingScheduleText(view: TextView, textHashMap: MutableLiveData<HashMap<String, Boolean>>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && textHashMap != null) {
        textHashMap.observe(parentActivity, Observer {
            for (key in it.keys) {
                view.text = key
                if (it[key]!!) view.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_lens_green_10dp, 0)
                else view.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_lens_red_10dp, 0)
            }
        })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("pagerAdapter")
fun setPagerAdapter(view: ViewPager, adapter: PagerAdapter) {
    view.adapter = adapter
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            if (value != "") {
                Picasso.get().load(value).fit().centerInside().placeholder(R.drawable.pin_logo_placeholder).into(view)
            }
        })
    }
}


@BindingAdapter("circleImageUrl")
fun setCircleImageUrl(view: ImageView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            if (value != "")
                Picasso.get().load(value).transform(CircleTransform()).into(view)
        })
    }
}

@BindingAdapter("imageResourceByName")
fun setImageResourceByName(view: ImageView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            if (value != "") {
                val resId = parentActivity.resources.getIdentifier(value, "drawable", parentActivity.packageName)
                view.setImageResource(resId)
            }
        })
    }
}