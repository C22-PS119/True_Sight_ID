package com.truesightid.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import java.util.*

class DetailImagesAdapter(val context: Context, val callback: DetailImagesCallback) :
    PagerAdapter() {
    private val images = ArrayList<String>()
    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)

    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val items = images[position]
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.item_row_images, container, false)

        // on below line we are initializing
        // our image and text view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.iv_image) as ImageView
        val textView: TextView = itemView.findViewById(R.id.tv_image_number) as TextView
        val nextArrow: ImageButton = itemView.findViewById(R.id.ib_next) as ImageButton
        val prevArrow: ImageButton = itemView.findViewById(R.id.ib_prev) as ImageButton

        // on below line we are setting
        // image resource for image view.
        Glide.with(itemView.context)
            .load(items)
            .timeout(3000)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.logo_true_sight)
            )
            .into(imageView)

        // on below line we are setting
        // text resource for text view.
        textView.text = "${position + 1}/${count}"

        // setup arrow
        if(position == 0 && count > 1){
            nextArrow.visibility = View.VISIBLE
            prevArrow.visibility = View.GONE
        }else if(position > 0 && count > 1){
            nextArrow.visibility = View.VISIBLE
            prevArrow.visibility = View.VISIBLE
        }else if(position == count && count >1){
            nextArrow.visibility = View.GONE
            prevArrow.visibility = View.VISIBLE
        }

        nextArrow.setOnClickListener {
            callback.onNextArrow(position + 1)
        }

        prevArrow.setOnClickListener {
            callback.onPrevArrow(position - 1)
        }

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    interface DetailImagesCallback {
        fun onNextArrow(position: Int)
        fun onPrevArrow(position: Int)
    }


}