package kz.nextstep.tazalykpartners.utils

import android.graphics.Typeface
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

@BindingAdapter("ratingProgress")
fun setRating(view: RatingBar, text: MutableLiveData<String>?){
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            //view.progress = it.toInt()
            view.rating = it.toFloat()
        })
    }

}

@BindingAdapter("buttonText")
fun setButtonText(view: Button, textHashmapSize: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && textHashmapSize != null) {
        textHashmapSize.observe(parentActivity, Observer {
            when(view.id) {
                R.id.btn_pin_detailed_info_see_all_comments -> {
                    if (it > 3) {
                        view.visibility = View.VISIBLE
                        view.text = "Посмотреть все комментарии($it)"
                    } else
                        view.visibility = View.GONE
                }
                else -> view.text = it.toString()
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
                if (it[key]!!) view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lens_green_10dp, 0)
                else view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lens_red_10dp, 0)
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

/*@BindingAdapter("toolbarTitle")
fun setToolbarTitle(view: Toolbar, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            view.title = it
        })
    }
}*/

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
                Picasso.get().load(value).fit().placeholder(R.drawable.user_placeholder_image).transform(CircleTransform()).into(view)
            else
                view.setImageResource(R.drawable.user_placeholder_image)
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
                when(view.id) {
                    R.id.iv_statistics_history_item_logo -> view.setColorFilter(parentActivity.resources.getColor(R.color.white))
                }
                view.setImageResource(resId)
            }
        })
    }
}

@BindingAdapter("progress")
fun setProgress(progressBar: ProgressBar, text: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = progressBar.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            progressBar.progress = it
        })
    }
}